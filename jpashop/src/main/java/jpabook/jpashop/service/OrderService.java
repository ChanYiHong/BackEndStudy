package jpabook.jpashop.service;

import jpabook.jpashop.domain.*;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import jpabook.jpashop.repository.MemberRepository;
import jpabook.jpashop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    // member 이름를 화면에 띄워야 해서 member 정보에 접근해야 함
    private final MemberRepository memberRepository;
    // Item 도 마찬가지.
    private final ItemRepository itemRepository;
    /**
     * 주문
     * 주문 화면에 보면 주문 회원 이름, 주문 상품명, 주문 수량이 필요함
     */
    @Transactional
    public Long order(Long memberId, Long itemId, int count) {

        // 엔티티 조회
        Member member = memberRepository.findOne(memberId);
        Item item = itemRepository.findOne(itemId);

        // 배송정보 생성
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());// 회원에 있는 주소의 값으로 배송한다. 간단하게. 실제로는 배송지 정보를 입력해야겠죠.
        delivery.setStatus(DeliveryStatus.READY);

        // 주문상품 생성
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);

        // 주문 생성
        Order order = Order.createOrder(member, delivery, orderItem);

        // 주문 저장
        orderRepository.save(order); // 이렇게 해줘도 cascade all 때문에 orderItem이랑 delivery도 다 저장됨. 자동으로 다 persist됨.

        return order.getId();
    }


    // 취소
    @Transactional
    public void cancelOrder(Long id) {
        // 주문 엔티티 조회
        Order order = orderRepository.findOne(id);
        // 주문 취소
        /**
         * entity 안의 데이터들만 바꿔주면 JPA가 알아서 바뀐 데이터들을
         * 더티 체킹이라 부르고 (변경내역감지)
         * 변경된 내역들을 다 찾아서, 데이터베이스에 업데이트 쿼리가 촥촥 날아간다.
         * JPA의 엄청 큰 장점임!
         *
         * 원래는..
         * 데이터를 다 끄집어내서 데이터를 파라미터 넣어서 다 쿼리로 다시 집어넣어야 하는데...
         * JPA 스타일은 최고임!
         */
        order.cancel();
    }

    // 검색
//    public List<Order> findeOrder(OrderSearch orderSearch){
//        return orderSearch.findAll(orderSearch);
//    }

}


/**

 이 case는 Order 만 OrderItem을 사용하고
 또, Order만 Delivery를 사용해서
 persist 해야 하는 life cycle이 완전히 똑같기 때문에
 cascade.all을 사용할 수 있음

 만약 아니면, repository를 따로 만들어서
 따로 persist를 해주는게 나음
 위에는 그냥 orderRepository.save(order) 로 다 저장됨

 **/