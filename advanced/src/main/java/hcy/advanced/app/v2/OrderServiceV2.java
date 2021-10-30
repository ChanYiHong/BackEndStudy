package hcy.advanced.app.v2;

import hcy.advanced.trace.TraceId;
import hcy.advanced.trace.TraceStatus;
import hcy.advanced.trace.hellotrace.HelloTraceV2;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceV2 {

    private final OrderRepositoryV2 orderRepositoryV2;
    private final HelloTraceV2 trace;

    public void orderItem(TraceId traceId, String itemId) {

        TraceStatus status = null;
        try {
            status = trace.beginSync(traceId,"OrderService.orderItem()");
            orderRepositoryV2.save(status.getTraceId(), itemId);
            trace.end(status);
        } catch (Exception e) {
            trace.exception(status, e);
            throw e;  // 예외를 꼭 다시 던져줘야 함. 로그 기능이 애플리케이션 흐름을 바꿔선 안된다.
            // 로그를 찍기위해 예외를 먹어버리면 안됨. 로그 때문에 예외가 사라지면 안된다.
        }
    }
}
