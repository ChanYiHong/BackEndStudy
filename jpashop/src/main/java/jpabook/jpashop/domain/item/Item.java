package jpabook.jpashop.domain.item;

import jpabook.jpashop.domain.Category;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

// 구현체를 사용할 것이기 때문에 추상 클래스로.
@Entity
// Single table 전략. 한 테이블에 다 때려박는 것.
// 상속 관계 하기 위해 설정을 꼭 해줘야 한다.
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
// 엘범, 책, 영화를 구분짓기 위함..?
@DiscriminatorColumn(name = "dtype")
@Getter @Setter
public abstract class Item {

    @Id @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;

    private int price;

    private int stockQuantity;

    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<>();

}
