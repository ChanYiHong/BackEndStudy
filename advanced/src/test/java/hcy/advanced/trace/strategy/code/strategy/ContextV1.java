package hcy.advanced.trace.strategy.code.strategy;

import lombok.extern.slf4j.Slf4j;

/**
 * 필드에 전략을 보관하는 방식
 */
@Slf4j
public class ContextV1 {

    private Strategy strategy;

    // 전략을 주입 받음.
    public ContextV1(Strategy strategy) {
        this.strategy = strategy;
    }

    // 기본적인 공통 로직.
    public void execute() {
        long startTime = System.currentTimeMillis();
        // 비즈니스 로직 실행
        strategy.call();
        // 비즈니스 로직 종료
        long endTime = System.currentTimeMillis();
        long result = endTime - startTime;
        log.info("resultTime={}", result);
    }

}
