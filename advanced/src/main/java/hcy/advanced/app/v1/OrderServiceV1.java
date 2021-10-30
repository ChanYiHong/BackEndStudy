package hcy.advanced.app.v1;

import hcy.advanced.trace.TraceStatus;
import hcy.advanced.trace.hellotrace.HelloTraceV1;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceV1 {

    private final OrderRepositoryV1 orderRepositoryV1;
    private final HelloTraceV1 trace;

    public void orderItem(String itemId) {

        TraceStatus status = null;
        try {
            status = trace.begin("OrderService.orderItem()");
            orderRepositoryV1.save(itemId);
            trace.end(status);
        } catch (Exception e) {
            trace.exception(status, e);
            throw e;  // 예외를 꼭 다시 던져줘야 함. 로그 기능이 애플리케이션 흐름을 바꿔선 안된다.
            // 로그를 찍기위해 예외를 먹어버리면 안됨. 로그 때문에 예외가 사라지면 안된다.
        }
    }
}
