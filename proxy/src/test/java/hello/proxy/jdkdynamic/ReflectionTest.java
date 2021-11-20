package hello.proxy.jdkdynamic;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Slf4j
public class ReflectionTest {

    @Test
    void reflection0() {
        Hello target = new Hello();

        // 공통 로직 1 시작
        log.info("start");
        String result1 = target.callA(); // 호출하는 메서드가 다름.
        log.info("result={}", result1);
        // 공통 로직 1 종료

        // 공통 로직 2 시작
        log.info("start");
        String result2 = target.callB(); // 호출하는 메서드가 다름.
        log.info("result={}", result2);
        // 공통 로직 2 종료
    }

    @Test
    void reflection1() throws Exception {
        // 클래스 정보. 내부 클래스는 $ 로 구분.
        Class classHello = Class.forName("hello.proxy.jdkdynamic.ReflectionTest$Hello");

        Hello target = new Hello();
        // callA 메서드 메타 정보
        Method methodCallA = classHello.getMethod("callA");
        // callA 메서드 호출
        Object result1 = methodCallA.invoke(target); // target 인스턴스에 있는 callA 호출.
        log.info("result1={}", result1);

        // callB 메서드 메타 정보
        Method methodCallB = classHello.getMethod("callB");
        Object result2 = methodCallB.invoke(target);
        log.info("result2={}", result2);
    }

    // reflection 을 활용해서 추상화! 하드 코딩 x
    // 공통화 시키기 매우 편리해 진다.
    @Test
    void reflection2() throws Exception{
        // 클래스 정보. 내부 클래스는 $ 로 구분.
        Class classHello = Class.forName("hello.proxy.jdkdynamic.ReflectionTest$Hello");

        Hello target = new Hello();
        Method methodCallA = classHello.getMethod("callA");
        dynamicCall(methodCallA, target);

        Method methodCallB = classHello.getMethod("callB");
        dynamicCall(methodCallB, target);
    }

    private void dynamicCall(Method method, Object target) throws Exception{
        log.info("start");
        Object result = method.invoke(target);
        log.info("result={}", result);
    }

    @Slf4j
    static class Hello {
        public String callA() {
            log.info("callA");
            return "A";
        }

        public String callB() {
            log.info("callB");
            return "B";
        }
    }

}
