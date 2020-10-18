package hello.review.xml;

import hello.review.member.MemberService;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

public class XmlAppContext {
    
    @Test
    public void xmlAppContext() throws Exception {
        //given
        ApplicationContext ac = new GenericXmlApplicationContext("appConfig.xml");

        //when
        MemberService memberService = ac.getBean("memberService", MemberService.class);

        //then
        assertThat(memberService).isInstanceOf(MemberService.class);
    }
}
