package hello.review;

import hello.review.member.Grade;
import hello.review.member.Member;
import hello.review.member.MemberService;
import hello.review.member.MemberServiceImpl;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class MemberApp {

    public static void main(String[] args) {

//        AppConfig appConfig = new AppConfig();
//
//        MemberService memberService = appConfig.memberService();

        // APpConfig에 있는 annotation 기반 환경설정을 보고, 스프링 컨테이너에 객체를 생성해서 관리해줌
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);
        // memberService 라고 되어있는 메소드를 실행해서 객체를 받아옴.
        MemberService memberService = applicationContext.getBean("memberService", MemberService.class);

        Member member = new Member(1L, "memberA", Grade.VIP);
        memberService.join(member);

        Member findMember = memberService.findMember(1L);
        System.out.println("new member = " + member.getName());
        System.out.println("find member = " + findMember.getName());
    }
}
