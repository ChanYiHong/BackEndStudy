package hello.proxy.common.advice;

import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

// 다른 프록시 방식과 다르게 target 클래스를 따로 주입 안받아도 됨
// 프록시 팩토리로 프록시를 생성하는 단계에서 이미 target 정보를 파라미터로 전달받아 MethodInvocation invocation안에 모두 포함
@Slf4j
public class TimeAdvice implements MethodInterceptor {
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        log.info("TimeProxy 실행");
        long startTime = System.currentTimeMillis();

        Object result = invocation.proceed(); // target 클래스를 호출하고 그 결과를 받는다.

        long endTime = System.currentTimeMillis();
        long resultTime = endTime - startTime;
        log.info("TimeProxy 종료 resultTIme={}", resultTime);
        return result;
    }
}
