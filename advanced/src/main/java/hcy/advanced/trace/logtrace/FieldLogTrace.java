package hcy.advanced.trace.logtrace;

import hcy.advanced.trace.TraceId;
import hcy.advanced.trace.TraceStatus;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FieldLogTrace implements LogTrace{

    private static final String START_PREFIX = "--->";
    private static final String COMPLETE_PREFIX = "<---";
    private static final String EX_PREFIX = "<X--";

    // TraceId 를 동기화하는 방식을 파라미터 방식에서 필드 방식으로.
    private TraceId traceIdHolder; // traceId 동기화. 동시성 이슈 발생.

    @Override
    public TraceStatus begin(String message) {
        syncTraceId();
        TraceId traceId = traceIdHolder;
        Long startTimeMs = System.currentTimeMillis();
        // 로그 출력.
        log.info("[{}] {}{}", traceId.getId(), addSpace(START_PREFIX, traceId.getLevel()) ,message);
        return new TraceStatus(traceId, startTimeMs, message);
    }

    private void syncTraceId() {
        // 최초 호출. traceId가 없으면 만듬.
        if (traceIdHolder == null) {
            traceIdHolder = new TraceId();
        }
        // 최초 호출이 아니면 level + 1.
        else {
            traceIdHolder = traceIdHolder.createNextId();
        }
    }

    @Override
    public void end(TraceStatus status) {
        complete(status, null);
    }

    @Override
    public void exception(TraceStatus status, Exception e) {
        complete(status, e);
    }

    private void complete(TraceStatus status, Exception e) {
        Long stopTimeMs = System.currentTimeMillis();
        long resultTimeMs = stopTimeMs - status.getStartTimeMs();
        TraceId traceId = status.getTraceId();

        if (e == null) {
            log.info("[{}] {}{} time={}ms", traceId.getId(), addSpace(COMPLETE_PREFIX, traceId.getLevel()), status.getMessage(), resultTimeMs);
        }
        else {
            log.info("[{}] {}{} time={}ms ex={}", traceId.getId(), addSpace(EX_PREFIX, traceId.getLevel()), status.getMessage(), resultTimeMs, e.toString());
        }

        releaseTraceId();
    }

    private void releaseTraceId() {
        if (traceIdHolder.isFirstLevel()) {
            traceIdHolder = null;  // destory. 레벨이 0이면 없앰.
        } else {
            traceIdHolder = traceIdHolder.createPreviousId();
        }
    }


    // level = 0
    // level = 1 |--->
    // level = 2 |   |--->
    // level = 2 ex |   |<X--
    private static String addSpace(String prefix, int level) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < level; i++) {
            sb.append((i == level - 1) ? "|" + prefix : "|   ");
        }
        return sb.toString();
    }

}
