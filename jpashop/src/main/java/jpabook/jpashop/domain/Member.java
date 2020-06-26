package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String name;

    // 내장타입
    @Embedded
    private Address address;

    // 1 : 다
    // 내가 mapping을 하는 애가 아니고, Order 테이블의 member 필드에 의해서 나는 맵핑된거야.
    // 읽기전용이 되는 것
    // 여기다 값을 넣는다고 해서 foreign key 값이 변경이 안됨
    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();

}
