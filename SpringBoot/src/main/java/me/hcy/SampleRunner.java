package me.hcy;


import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class SampleRunner implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("러너러너..");
    }
}


/**

 애플리케이션이 다 뜬 다음에 뭔가를 더 추가적으로 실행을 하고 싶다
 할 때 쓸 수 있는 것이 ApplicationRunner;

 **/

