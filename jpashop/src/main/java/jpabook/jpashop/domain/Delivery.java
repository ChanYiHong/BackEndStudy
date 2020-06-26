package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
public class Delivery {

    @Id @GeneratedValue
    @Column(name = "delivery_id")
    private Long id;

    @OneToOne(mappedBy = "delivery", fetch = FetchType.LAZY)
    private Order order;

    @Embedded
    private Address address;

    // 꼭 STRING 으로! Ordinal로 하면 새로 추가 되었을 때 순서가밀리면 망함.
    @Enumerated(EnumType.STRING)
    private DeliveryStatus status; // READY, COMP
}
