package jpabook.jpashop.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {

    @Id
    @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    // 다 : 1
    @ManyToOne(fetch = FetchType.LAZY)
    // 맵핑을 뭘로 할꺼냐. foreign key 이름이 member_id
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL) // Order가 persist될 때 delivery도 persist 된다.
    private List<OrderItem> orderItems = new ArrayList<>();

    // 일대일 맵핑에서는 액세스가 많이 되는 곳을 foreign key로
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    private LocalDateTime orderDate;  //주문시간

    @Enumerated(EnumType.STRING)
    private OrderStatus status;  //주문상태 [ORDER, CANCEL]


    //== 연관관계 메서드==//
    // 멤버가 오더를 하면 값을 넣어줘야 함
    // 양쪽에 값을 다 세팅해 줘야 함
    public void setMember(Member member) {
        this.member = member;
        member.getOrders().add(this);
    }

    /**
    이 코드를 자동으로 해주는게 연관관계 메서드.

    public static void main(String[] args) {
     Member member = new Member();
     Order order = new Order();

     member.getOrders().add(order);
     order.setMember(member);
     }

     **/
    public void addOrderItem(OrderItem orderItem){
        this.orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
        delivery.setOrder(this);
    }


    // == 생성 메서드 == //
    /**
     * 주문 생성이 복잡함. oederItem, delivery.. 복잡해짐
     * 별도의 생성 메서드가 있으면 좋다.
     * 이렇게 만들어 두면 생성하는 지점을 변경할 때 이 method만 바꾸면 됨
     */
    public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems){ // ... 문법으로 여러개 넘기기?
        Order order = new Order();
        order.setMember(member);
        order.setDelivery(delivery);
        for(OrderItem orderItem : orderItems){
            order.addOrderItem(orderItem);
        }
        order.setStatus(OrderStatus.ORDER);
        order.setOrderDate(LocalDateTime.now());
        return order;
    }


    // == 비즈니스 로직 == //

    /**
     *
     * 주문 취소
     */
    public void cancel() {
        // delivery의 상태가 배송완료면.
        if(delivery.getStatus() == DeliveryStatus.COMP) {
            throw new IllegalStateException("이미 배송완료된 상품은 취소가 불가능합니다.");
        }
        this.setStatus(OrderStatus.CANCEL);
        // OrderItem도 주문을 취소 시켜야 함.
        // 주문 하나당 order가 여러개 였을 수 있으니, 주문 각각에다가 cancel을 날려주는 거에요.
        for(OrderItem orderItem : orderItems) {
            orderItem.cancel();
        }
    }

    // == 조회 로직 == //
    // 계산이 필요할 때가 있음

    /**
     *
     * 전체 주문 가격 조회
     */
    public int getTotalPrice() {
        int totalPrice = 0;
        for(OrderItem orderItem : this.orderItems){
            // 주문 수량 x 가격.
            totalPrice += orderItem.getTotalPrice();
        }
        return totalPrice;
    }

}
