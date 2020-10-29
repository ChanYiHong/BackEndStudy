package hello.review.order;

import hello.review.discount.DiscountPolicy;
import hello.review.discount.FixDiscountPolicy;
import hello.review.discount.RateDiscountPolicy;
import hello.review.member.Member;
import hello.review.member.MemberRepository;
import hello.review.member.MemoryMemberRepository;

public class OrderServiceImpl implements OrderService{

    private final MemberRepository memberRepository;
    private final DiscountPolicy discountPolicy;

    public OrderServiceImpl(MemberRepository memberRepository, DiscountPolicy discountPolicy) {
        this.memberRepository = memberRepository;
        this.discountPolicy = discountPolicy;
    }

    @Override
    public Order createOrder(Long memberId, String itemName, int itemPrice) {
        // 회원 정보를 먼저 조회.
        Member member = memberRepository.findById(memberId);
        // 할인된 금액을 받음.
        int discountPrice = discountPolicy.discount(member, itemPrice);

        // Order를 반환.
        return new Order(memberId, itemName, itemPrice, discountPrice);
    }

    // @Configuration 테스트 용도.
    public MemberRepository getMemberRepository(){
        return this.memberRepository;
    }
}
