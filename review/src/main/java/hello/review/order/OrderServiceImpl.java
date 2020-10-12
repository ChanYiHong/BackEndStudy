package hello.review.order;

import hello.review.discount.DiscountPolicy;
import hello.review.discount.FixDiscountPolicy;
import hello.review.member.Member;
import hello.review.member.MemberRepository;
import hello.review.member.MemoryMemberRepository;

public class OrderServiceImpl implements OrderService{

    private final MemberRepository memberRepository = new MemoryMemberRepository();
    private final DiscountPolicy discountPolicy = new FixDiscountPolicy();

    @Override
    public Order createOrder(Long memberId, String itemName, int itemPrice) {
        // 회원 정보를 먼저 조회.
        Member member = memberRepository.findById(memberId);
        // 할인된 금액을 받음.
        int discountPrice = discountPolicy.discount(member, itemPrice);

        // Order를 반환.
        return new Order(memberId, itemName, itemPrice, discountPrice);
    }
}
