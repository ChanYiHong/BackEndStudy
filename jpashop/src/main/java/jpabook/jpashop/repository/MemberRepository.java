package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Member;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

// Spring Bean 으로 Spring이 등록
@Repository
public class MemberRepository {

    // JPA의 EntityManager를 Spring을 주입해줌
    @PersistenceContext
    private EntityManager em;

    // 멤버 등록
    // 영속성 context에 일단 member entity를 넣죠
    // 나중에 트렌젝션이 커밋 되는 시점에 DB에 반영
    // DB에 instant query가 날라가는 겁니다.
    public void save(Member member) {
        em.persist(member);
    }

    // 한명씩 회원 가져오기
    // JPA의 find method를 사용하는데, 단건 조회에요
    // 첫 번쨰는 type, 두 번째 인자는 Pk
    public Member findOne(Long id) {
        return em.find(Member.class, id);
    }

    // 회원 목록 한번에 가져오기
    public List<Member> findAll() {
        // entity 객체에 대해 query
        return em.createQuery("select m from Member m", Member.class)
        .getResultList();
    }

    // 이름으로 회원 찾기
    // 파라미터 바인딩에 의해서 특정 회원에 대한 이름으로 회원을 찾는 방법
    public List<Member> findByName(String name) {
        return em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", name)
                .getResultList();
    }
}
