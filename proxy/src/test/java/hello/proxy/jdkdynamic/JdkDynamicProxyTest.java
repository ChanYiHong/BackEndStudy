package hello.proxy.jdkdynamic;

import hello.proxy.jdkdynamic.code.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Proxy;

@Slf4j
public class JdkDynamicProxyTest {

    @Test
    void dynamicA(){
        AInterface target = new AImpl();

        TimeInvocationHandler handler = new TimeInvocationHandler(target);

        // 프록시 동적 생성. java 언어 차원에서 생성.
        // 클래스가 어디에 생성 될 지 classLoader 생성.
        // 어떤 인터페이스 기반으로 만들지
        // 프록시가 사용해야 할 로직
        AInterface proxy = (AInterface) Proxy.newProxyInstance(AInterface.class.getClassLoader(), new Class[]{AInterface.class}, handler);

        proxy.call(); // handler에 있는 로직을 수행.
        log.info("targetClass={}", target.getClass());
        log.info("proxyClass={}", proxy.getClass());
    }

    @Test
    void dynamicB(){
        BInterface target = new BImpl();

        TimeInvocationHandler handler = new TimeInvocationHandler(target);

        // 프록시 동적 생성. java 언어 차원에서 생성.
        // 클래스가 어디에 생성 될 지 classLoader 생성.
        // 어떤 인터페이스 기반으로 만들지
        // 프록시가 사용해야 할 로직
        BInterface proxy = (BInterface) Proxy.newProxyInstance(BInterface.class.getClassLoader(), new Class[]{BInterface.class}, handler);

        proxy.call(); // handler에 있는 로직을 수행.
        log.info("targetClass={}", target.getClass());
        log.info("proxyClass={}", proxy.getClass());
    }

}
