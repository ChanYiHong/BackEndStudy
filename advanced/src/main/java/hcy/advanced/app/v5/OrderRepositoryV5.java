package hcy.advanced.app.v5;

import hcy.advanced.trace.callback.TraceTemplate;
import hcy.advanced.trace.logtrace.LogTrace;
import hcy.advanced.trace.template.AbstractTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
public class OrderRepositoryV5 {

    private final TraceTemplate traceTemplate;

    public OrderRepositoryV5(LogTrace trace) {
        this.traceTemplate = new TraceTemplate(trace);
    }

    // 상품을 저장할 때 1초의 시간이 걸리고, 만약에 "ex"라는 이름의 아이템이 들어오면 예외 발생.
    public void save(String itemId) {

        traceTemplate.execute("OrderRepository.save()", () -> {
            if (itemId.equals("ex")) {
                throw new IllegalStateException("예외 발생");
            }
            sleep(1000);
            return null;
        });
    }

    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
