package study.datajpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.entity.Member;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberRepositoryTest {

    @Autowired MemberRepository memberRepository;

    @Test
    public void testMember() {
        Member member = new Member("user1");
        Member saveMember = memberRepository.save(member);
        // 있을 수도 있고, 없을 수도 있어서 Optional로 제공.
        Member findMember = memberRepository.findById(saveMember.getId()).get();

        assertThat(saveMember).isEqualTo(findMember);
    }

}