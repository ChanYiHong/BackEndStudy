package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter @Setter
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

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
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

}
