package hcy.advanced;

import hcy.advanced.trace.logtrace.FieldLogTrace;
import hcy.advanced.trace.logtrace.LogTrace;
import hcy.advanced.trace.logtrace.ThreadLocalLogTrace;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LogTraceConfig {

    // 싱글톤으로 스프링 빈으로 등록됨.
    @Bean
    public LogTrace logTrace(){
        return new ThreadLocalLogTrace();
    }

}
