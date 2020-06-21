package me.hcy.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("prod")
@Configuration
public class BaseConfiguration {

    @Bean
    public String hello() {
        return "hello";
    }

}


/**

 Bean 설정파일 자체가 prod라는 profile일 때 작성이 된다
 prod라는 profile이 아니면 이 Bean 설정파일은 사용이 안된다.

 **/