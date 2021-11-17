package hcy.advanced.app.v5;

import hcy.advanced.trace.callback.TraceTemplate;
import hcy.advanced.trace.logtrace.LogTrace;
import hcy.advanced.trace.template.AbstractTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController  // @Controller + @ResponseBody
public class OrderControllerV5 {

    private final OrderServiceV5 orderServiceV5;
    private final TraceTemplate traceTemplate;

    public OrderControllerV5(OrderServiceV5 orderServiceV5, LogTrace trace) {
        this.orderServiceV5 = orderServiceV5;
        this.traceTemplate = new TraceTemplate(trace);
    }

    @GetMapping("/v5/request")
    public String request(String itemId) {

        // callback 넘겨주기.
        return traceTemplate.execute("OrderController.request", () -> {
            orderServiceV5.orderItem(itemId);
            return "ok";
        });

    }

}
