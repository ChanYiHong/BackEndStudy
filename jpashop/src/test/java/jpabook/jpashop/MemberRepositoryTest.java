package jpabook.jpashop;

import org.assertj.core.api.Assertions;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;


// Junit한테 나 Spring과 관련된 거로 테스트할거야 라고 알려줘야 하고
@RunWith(SpringRunner.class)
// SpringBoot로 테스트
@SpringBootTest
public class MemberRepositoryTest {

    @Autowired MemberRepository memberRepository;

    // TDD
    @Test
    @Transactional
    @Rollback(false)
    public void testMember() throws Exception {

        // Member를 가지고.
        //given
        Member member = new Member();
        member.setUsername("memberA");

        // Save를 하면.
        //when
        Long saveId = memberRepository.save(member);
        Member findMember = memberRepository.find(saveId);

        // 내가 Save한게 잘 저장되었는지 확인.
        //then
        Assertions.assertThat(findMember.getId()).isEqualTo(member.getId());
        Assertions.assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        // 영속성 context에서 식별자가 같으면 같은 entity라고 보면 됨
        Assertions.assertThat(findMember).isEqualTo(member);
    }
}

/**

 이렇게만 하면 오류가 뜬다.
 entity manager를 통한 모든 데이터 변경은 항상 Transaction안에서 이루어져야 함

 **/