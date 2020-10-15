package hello.review;

import hello.review.discount.FixDiscountPolicy;
import hello.review.member.MemberService;
import hello.review.member.MemberServiceImpl;
import hello.review.member.MemoryMemberRepository;
import hello.review.order.OrderService;
import hello.review.order.OrderServiceImpl;

public class AppConfig {

    public MemberService memberService() {
        return new MemberServiceImpl(new MemoryMemberRepository());
    }

    public OrderService orderService() {
        return new OrderServiceImpl(new MemoryMemberRepository(), new FixDiscountPolicy());
    }
}
