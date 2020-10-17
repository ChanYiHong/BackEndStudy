package hello.review.beanfind;

import hello.review.AppConfig;
import hello.review.member.Member;
import hello.review.member.MemberService;
import hello.review.member.MemberServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ApplicationContextBeanFindTest {

    AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);

    @Test
    @DisplayName("Bean 이름으로 찾기")
    public void findBeanByName() throws Exception {
        //given
        MemberService memberService = ac.getBean("memberService", MemberService.class);

        //when

        //then
        System.out.println("memberService = " + memberService);
        System.out.println("memberService.getClass() = " + memberService.getClass());

        assertThat(memberService).isInstanceOf(MemberService.class);
    }

    @Test
    @DisplayName("이름 없이 타입으로 조회")
    public void findBeanByType() throws Exception {
        //given
        MemberService memberService = ac.getBean(MemberService.class);

        //when

        //then
        System.out.println("memberService = " + memberService);
        System.out.println("memberService.getClass() = " + memberService.getClass());

        assertThat(memberService).isInstanceOf(MemberService.class);
    }


    @Test
    @DisplayName("구체 타입으로 조회.")
    public void findBeanByName2() throws Exception {
        //given
        MemberServiceImpl memberService = ac.getBean("memberService", MemberServiceImpl.class);

        //when

        //then
        System.out.println("memberService = " + memberService);
        System.out.println("memberService.getClass() = " + memberService.getClass());

        assertThat(memberService).isInstanceOf(MemberService.class);
    }
    
    
    @Test
    @DisplayName("빈 이름으로 조회X")
    public void findBeanByNameX() throws Exception {
        // 예외가 터져야 함.
        assertThrows(NoSuchBeanDefinitionException.class,
                () -> ac.getBean("xxxx", MemberService.class));
    }

}
