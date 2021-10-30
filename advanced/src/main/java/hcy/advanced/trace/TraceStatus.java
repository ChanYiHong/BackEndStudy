package hcy.advanced.trace;

import lombok.Getter;

// 로그의 상태 정보.
@Getter
public class TraceStatus {

    private TraceId traceId;
    private Long startTimeMs;  // 시작시간을 알면 끝나는 시간도 계산 가능.
    private String message; // 시작시 사용한 메세지. 이후 로그 종료시에도 이 메세지를 사용해 출력.

    public TraceStatus(TraceId traceId, Long startTimeMs, String message) {
        this.traceId = traceId;
        this.startTimeMs = startTimeMs;
        this.message = message;
    }

}
