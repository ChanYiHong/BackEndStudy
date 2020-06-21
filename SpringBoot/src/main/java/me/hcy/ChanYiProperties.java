package me.hcy;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import java.time.Duration;

@Component
@ConfigurationProperties("chanyi")
@Validated
public class ChanYiProperties {

    @NotEmpty
    private String name;
    private int age;
    private String fullName;

    private Duration sessoinTimeout = Duration.ofSeconds(30);

    public Duration getSessoinTimeout() {
        return sessoinTimeout;
    }

    public void setSessoinTimeout(Duration sessoinTimeout) {
        this.sessoinTimeout = sessoinTimeout;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}

/**

 빈으로 만들고, type safe하게 만들고..
 type safe하다는건,
 @Value("${chanyi.name}")
 이렇게 여기서 오타 날 수도 있는데,

 그냥 주입받아서 getName 이런거 쓰면 됨

 물론, properties파일 자체는 type safe하지 않을 수도 있음

 ----------------------------------

 properties의 값들을 검증하는 방법도 있음
 @Validated

 @NotEmpty같은거로
 Failure Analyzer가 아주 이쁘게 오류 메세지를 보내준다

 -----------------------------------

 프로파일?
 어떠한 특정 profile일 때 Bean 설정을 다르게 하고 싶다 할 때 쓴다

 properties에다가 활성화 하는 프로파일을 설정 할 수 있음
 spring.profiles.active = prod
 이런식
 이런 property의 우선순위에 적용이 된다

 아니면 prod 용 property를 넣는 곳도 만들 수 있음
 우선순위가 기본 application.property보다 더 높음

 spring.profiles.include = proddb
 이 설정이 읽혀졌을 때 추가할 profile을 활성화 하는 코드

 program argument에다가
 --spring.profiles.active=prod
 라고 argument로 실행할 수 있음

 ---------------------------------------

 **/