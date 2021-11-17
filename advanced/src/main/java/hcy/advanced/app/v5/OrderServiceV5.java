package hcy.advanced.app.v5;

import hcy.advanced.trace.callback.TraceTemplate;
import hcy.advanced.trace.logtrace.LogTrace;
import hcy.advanced.trace.template.AbstractTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceV5 {

    private final OrderRepositoryV5 orderRepositoryV5;
    private final TraceTemplate traceTemplate;

    public OrderServiceV5(OrderRepositoryV5 orderRepositoryV5, LogTrace trace) {
        this.orderRepositoryV5 = orderRepositoryV5;
        this.traceTemplate = new TraceTemplate(trace);
    }

    public void orderItem(String itemId) {

        traceTemplate.execute("OrderService.orderItem()", () -> {
            orderRepositoryV5.save(itemId);
            return null;
        });
    }
}
