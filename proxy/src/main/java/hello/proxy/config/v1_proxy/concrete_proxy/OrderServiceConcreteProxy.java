package hello.proxy.config.v1_proxy.concrete_proxy;

import hello.proxy.app.v2.OrderRepositoryV2;
import hello.proxy.app.v2.OrderServiceV2;
import hello.proxy.trace.TraceStatus;
import hello.proxy.trace.logtrace.LogTrace;

public class OrderServiceConcreteProxy extends OrderServiceV2 {

    private final OrderServiceV2 target;
    private final LogTrace logTrace;

    public OrderServiceConcreteProxy(OrderServiceV2 target, LogTrace logTrace) {
        // 자바 기본 문법상 자식 클래스는 부모 클래스의 생성자를 호출해야 함. 기본적으로 기본생성자를 호출하지만, 부모 클래스에 기본 생성자가 없음.
        super(null); // 부모의 기능은 안쓰고 프록시 기능만 사용할 것이기 때문에 자바 문법상 null을 넣어줘야 함..
        this.target = target;
        this.logTrace = logTrace;
    }

    @Override
    public void orderItem(String itemId) {
        TraceStatus status = null;
        try {
            status = logTrace.begin("OrderService.orderItem()");
            // target 호출.
            target.orderItem(itemId);
            logTrace.end(status);
        } catch (Exception e) {
            logTrace.exception(status, e);
            throw e;
        }
    }
}
