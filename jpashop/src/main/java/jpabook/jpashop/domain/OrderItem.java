package jpabook.jpashop.domain;

import jpabook.jpashop.domain.item.Item;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
@Table(name = "order_item")
// 이게 왜쓰냐면, 다른 package의 class에서 OrderItem temp = new OrderItem()을 못하게 막는 것. 유지보수가 어려워지기때문
// 비즈니스 로직은 되도록 응집도 있게 !!, 여기서는 createOrderItem을 사용
// 항상 코드를 제약하는 상태로 짜세요. 좋은 유지보수 가능.
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem {

    @Id @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    // 1 : 다에서 '다' 쪽에 foreign key가 들어온다
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private int orderPrice; // 주문 당시의 가격

    private int count; // 주문 당시의 수량


    // == 생성 메서드 == //
    // 할인 같은게 있을 수 있어서, item의 price랑 orderPrice랑 따로 가져가는게 나음
    public static OrderItem createOrderItem(Item item, int orderPrice, int count) {
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setOrderPrice(orderPrice);
        orderItem.setCount(count);

        // 넘어 온 것 만큼 재고를 까버림
        item.removeStock(count);
        return orderItem;
    }



    // == 비즈니스 로직 == //
    // 재고 수량을 원상복구 시킨다.
    public void cancel() {
        getItem().addStock(count);
    }


    // == 조회 로직 == //
    // 주문 상품 전체 가격 조회
    // 주문할 때 주문 가격과 수량을 곱해야 하기 때문
    public int getTotalPrice() {
        return getOrderPrice() * getCount();
    }
}
