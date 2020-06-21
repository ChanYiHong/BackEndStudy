package me.hcy;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class SampleRunner implements ApplicationRunner {

//    @Value("${chanyi.name}")
//    private String name;
//
//    @Value("${chanyi.age}")
//    private int age;

    @Autowired
    ChanYiProperties chanYiProperties;

    @Autowired
    private String hello;

    @Override
    public void run(ApplicationArguments args) throws Exception {
//        System.out.println("러너러너..");
//        System.out.println(name);
//        System.out.println(age);

        System.out.println(chanYiProperties.getName());
        System.out.println(chanYiProperties.getFullName());
        System.out.println(chanYiProperties.getSessoinTimeout());

        System.out.println(hello);
    }
}


/**

 애플리케이션이 다 뜬 다음에 뭔가를 더 추가적으로 실행을 하고 싶다
 할 때 쓸 수 있는 것이 ApplicationRunner;

 -----------------------------------
 외부 설정 파일
 property들 key value pair로 제공됨

 application.properties  -> 15위의 우선순위!

 ------------------------------------

 @Value는 applicaton.properties를 받는거고..

 @Autowired로
 ChanyiProperties를 주입을 받고,
 거기 있는 메소드들을 사용


 -------------------

 type conversion도 일어남
 알아서 해당 type으로 바인딩 해줌
 Duration type예시
 **/

