package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.Assert.*;

// Junit 실행할 때 spring이랑 같이 실행할래 하는
@RunWith(SpringRunner.class)
// Spring Boot 를 띄운 상태로 테스트. spring contatiner안에서 이 테스트를 돌림
@SpringBootTest
// 데이터 변경을 위함. 이게 있어야 롤백이 됨
@Transactional
public class MemberServiceTest {

    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;
    @Autowired EntityManager em;

    @Test
    //@Rollback(false)
    public void 회원가입() throws Exception {

        // given (이런게 주어졌을때)
        Member member = new Member();
        member.setName("Hong");
        // when  (이걸 실행하면)
        Long savedId = memberService.join(member);
        // then  (이렇게 나와야해)

        // 영속성 컨텍스트에 있는 데이터를 db에 저장. 롤벡 어노테이션 안 쓸때 사용가능
        // em.flush();
        assertEquals(member, memberRepository.findOne(savedId));

    }

    @Test(expected = IllegalStateException.class)
    public void 중복_회원_예외() throws Exception {

        // given
        Member member1 = new Member();
        member1.setName("Hong");
        Member member2 = new Member();
        member2.setName("Hong");
        // when

        Long savedId1 = memberService.join(member1);
        Long savedId2 = memberService.join(member2); // 예외가 발생해야 함

        // 예외가 발생하면 이 로직을 나가게 됨
        // 밑까지 가버리면 안됨
        // expected = IllegalStateException.class를 해두면 IllegalStateException이 뜰 때 정상적으로 테스트가 동작한거로 간주
        // IllegalStateException은 Member Service의 validation check에 있음

        // then
        // 여기까지 오면 안됨. fail
        fail("예외가 발생해야 합니다.");
    }

}