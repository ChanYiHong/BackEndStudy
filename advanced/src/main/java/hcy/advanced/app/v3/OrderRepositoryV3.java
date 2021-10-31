package hcy.advanced.app.v3;

import hcy.advanced.trace.TraceId;
import hcy.advanced.trace.TraceStatus;
import hcy.advanced.trace.hellotrace.HelloTraceV2;
import hcy.advanced.trace.logtrace.LogTrace;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryV3 {

    private final LogTrace trace;

    // 상품을 저장할 때 1초의 시간이 걸리고, 만약에 "ex"라는 이름의 아이템이 들어오면 예외 발생.
    public void save(String itemId) {

        TraceStatus status = null;
        try {
            status = trace.begin("OrderRepository.save()");

            // 저장 로직
            if (itemId.equals("ex")) {
                throw new IllegalStateException("예외 발생");
            }
            sleep(1000);

            trace.end(status);
        } catch (Exception e) {
            trace.exception(status, e);
            throw e;  // 예외를 꼭 다시 던져줘야 함. 로그 기능이 애플리케이션 흐름을 바꿔선 안된다.
            // 로그를 찍기위해 예외를 먹어버리면 안됨. 로그 때문에 예외가 사라지면 안된다.
        }

    }

    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
