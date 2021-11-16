package hcy.advanced.trace.strategy;

import hcy.advanced.trace.strategy.code.strategy.ContextV1;
import hcy.advanced.trace.strategy.code.strategy.Strategy;
import hcy.advanced.trace.strategy.code.strategy.StrategyLogic1;
import hcy.advanced.trace.strategy.code.strategy.StrategyLogic2;
import hcy.advanced.trace.template.code.AbstractTemplate;
import hcy.advanced.trace.template.code.SubClassLogic1;
import hcy.advanced.trace.template.code.SubClassLogic2;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class ContextV1Test {

    @Test
    void strategyV0(){
        logic1();
        logic2();
    }

    // 부가기능과 핵심기능이 섞여있음.
    private void logic1() {
        long startTime = System.currentTimeMillis();
        // 비즈니스 로직 실행
        log.info("비즈니스 로직1 실행");
        // 비즈니스 로직 종료
        long endTime = System.currentTimeMillis();
        long result = endTime - startTime;
        log.info("resultTime={}", result);
    }

    private void logic2() {
        long startTime = System.currentTimeMillis();
        // 비즈니스 로직 실행
        log.info("비즈니스 로직1 실행");
        // 비즈니스 로직 종료
        long endTime = System.currentTimeMillis();
        long result = endTime - startTime;
        log.info("resultTime={}", result);
    }

    @Test
    void strategyV1(){
        Strategy strategyLogic1 = new StrategyLogic1();
        // 원하는 모양으로 조립.
        ContextV1 context1 = new ContextV1(strategyLogic1);
        context1.execute();

        Strategy strategyLogic2 = new StrategyLogic2();
        ContextV1 context2 = new ContextV1(strategyLogic2);
        context2.execute();
    }

    // 익명 내부 클래스 사용.
    @Test
    void strategyV2(){
        ContextV1 context1 = new ContextV1(() -> log.info("비즈니스 로직 1 실행"));
        ContextV1 context2 = new ContextV1(() -> log.info("비즈니스 로직 2 실행"));
        context1.execute();
        context2.execute();
    }


}
