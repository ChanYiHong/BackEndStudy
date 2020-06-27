package jpabook.jpashop.domain.item;

import jpabook.jpashop.domain.Category;
import jpabook.jpashop.exception.NotEnoughStockException;
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


    // ==비즈니스 로직==//
    // 객체지향적. stockQuantity가 이 entity 안에 있음
    // 이 데이터를 가지고 있는 쪽에서 비즈니스 로직이 나가는게 응집도가 있음

    /**
     *
     * stock 증가.
     */

    public void addStock(int quantity) {
        this.stockQuantity += quantity;
    }

    /**
     *
     * stock 감소.
     */

    public void removeStock(int quantity) {
        int restStock = this.stockQuantity - quantity;
        if (restStock < 0) {
            throw new NotEnoughStockException("need more stock");
        }
        this.stockQuantity = restStock;
    }
}
