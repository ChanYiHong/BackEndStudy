package hello.review.order;

import hello.review.AppConfig;
import hello.review.member.Grade;
import hello.review.member.Member;
import hello.review.member.MemberService;
import hello.review.member.MemberServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

public class OrderServiceTest {

    ApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);
    MemberService memberService;
    OrderService orderService;

    @BeforeEach
    public void beforeEach(){
        memberService = ac.getBean("memberService", MemberService.class);
        orderService = ac.getBean("orderService", OrderService.class);
    }

    @Test
    public void createOrder() throws Exception {
        //given
        Long memberId = 1L;
        Member member = new Member(memberId, "memberA", Grade.VIP);
        memberService.join(member);
        //when

        Order order = orderService.createOrder(memberId,"itemA", 10000);

        //then
        assertThat(order.getDiscountPrice()).isEqualTo(1000);

    }
}
