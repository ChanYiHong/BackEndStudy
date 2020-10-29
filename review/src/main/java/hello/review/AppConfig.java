package hello.review;

import hello.review.discount.DiscountPolicy;
import hello.review.discount.FixDiscountPolicy;
import hello.review.discount.RateDiscountPolicy;
import hello.review.member.MemberRepository;
import hello.review.member.MemberService;
import hello.review.member.MemberServiceImpl;
import hello.review.member.MemoryMemberRepository;
import hello.review.order.OrderService;
import hello.review.order.OrderServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public MemberService memberService() {
        System.out.println("call AppConfig.memberService");
        return new MemberServiceImpl(memberRepository());
    }

    @Bean
    public MemberRepository memberRepository() {
        System.out.println("call AppConfig.memberRepository");
        return new MemoryMemberRepository();
    }

    @Bean
    public OrderService orderService() {
        System.out.println("call AppConfig.orderService");
        return new OrderServiceImpl(memberRepository(), discountPolicy());
    }

    @Bean
    public DiscountPolicy discountPolicy() {
        return new RateDiscountPolicy();
    }
}
