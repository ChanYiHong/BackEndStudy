package hello.review.order;

import hello.review.member.Grade;
import hello.review.member.Member;
import hello.review.member.MemberService;
import hello.review.member.MemberServiceImpl;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class OrderServiceTest {

    MemberService memberService = new MemberServiceImpl();
    OrderService orderService = new OrderServiceImpl();

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
