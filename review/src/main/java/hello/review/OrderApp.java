package hello.review;

import hello.review.member.Grade;
import hello.review.member.Member;
import hello.review.member.MemberService;
import hello.review.member.MemberServiceImpl;
import hello.review.order.Order;
import hello.review.order.OrderService;
import hello.review.order.OrderServiceImpl;

public class OrderApp {

    public static void main(String[] args) {

        MemberService memberService = new MemberServiceImpl();
        OrderService orderService = new OrderServiceImpl();

        Long memberId = 1L;
        Member member = new Member(memberId, "memberA", Grade.VIP);
        memberService.join(member);

        Order order = orderService.createOrder(memberId, "itemA", 10000);

        System.out.println("order = " + order);
    }
}
