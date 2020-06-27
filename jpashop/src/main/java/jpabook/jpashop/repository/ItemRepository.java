package jpabook.jpashop.repository;

import jpabook.jpashop.domain.item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepository {

    // persistence context를 안쓰고, 컨스트럭터로 엔티티매니저 주입
    private final EntityManager em;

    /**
     * 상품 저장
     */
    public void save(Item item) {

        // 아이템은 JPA에 저장될 때 까지 ID 값이 없음
        // 아이디 값이 없다는 건 완전히 새로 생성한 객체
        if(item.getId() == null) {
            // 새로운 객체로 등록
            em.persist(item);
        } else {
            // 아이템 값이 있네? 이건 이미 디비에 등록이 된 것을 가져온 것. 업데이트라고 생각해주면 됨. 나중에 설명
            em.merge(item);
        }

    }

    // 아이템 하나 찾기. 조회
    public Item findOne(Long id) {
        return em.find(Item.class, id);
    }

    // 아이템 여러개 찾기. JPQL 작성 필요
    public List<Item> findAll() {
        return em.createQuery("select i from Item i", Item.class)
                .getResultList();
    }

}
