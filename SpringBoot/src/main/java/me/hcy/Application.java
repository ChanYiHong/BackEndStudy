package me.hcy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
//@EnableConfigurationProperties(ChanYiProperties.class)
public class Application {

    public static void main(String[] args) {
        //SpringApplication.run(Application.class, args);
        SpringApplication app = new SpringApplication(Application.class);
        //app.addListeners(new SampleListener());

        app.setWebApplicationType(WebApplicationType.SERVLET);
        app.run(args);
    }
}

/**

 기본적인 log level은 INFO level임

 ApplicationContext를 만들기 전에 발생하는 거냐 후에 발생하는거냐에 따라
 event 등록하는 방법이 달라진다.

 기본적으로 WebApplicationType 은 servlet임
 (webflux랑 같이있어도 서블릿이 기본.)
 (webflux 쓰고 싶으면 REACTIVE로)

 JVM option은 application argument가 아님
 오로지 -- 준 애들이 application argument임

 ----------------------------

 EnableConfigurationProperties

 ConfigurationProperties를 달고있는 class를 원래 여기다 줘야 함
 근데 이제 자동으로 기본적으로 등록이 된다

 우리가 해야 할일은 Properties class에
 component 어노테이션을 달면 됨

 **/
