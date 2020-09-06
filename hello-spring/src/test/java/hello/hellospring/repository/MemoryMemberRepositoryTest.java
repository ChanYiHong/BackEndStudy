package hello.hellospring.repository;

import hello.hellospring.domain.Member;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class MemoryMemberRepositoryTest {

    MemoryMemberRepository repository = new MemoryMemberRepository();

    @AfterEach
    public void afterEach(){
        repository.clearStore();
    }

    @Test
    public void save(){
        Member member = new Member();
        member.setName("spring");

        repository.save(member);

        // Optional에서 끄낼때 get으로.. 사실 좋은 방법은 아님.
        Member result = repository.findById(member.getId()).get();

        assertThat(member).isEqualTo(result);
    }

    @Test
    public void find() throws Exception {
        //given
        Member member1 = new Member();
        member1.setName("spring1");
        repository.save(member1);

        Member member2 = new Member();
        member2.setName("spring2");
        repository.save(member2);

        //when

        Member result = repository.findByName("spring1").get();

        //then

        assertThat(result).isEqualTo(member1);

    }

    @Test
    public void findAll() throws Exception {
        //given

        Member member1 = new Member();
        member1.setName("spring1");
        repository.save(member1);

        Member member2 = new Member();
        member2.setName("spring2");
        repository.save(member2);

        //when

        List<Member> members = repository.findAll();

        //then

        assertThat(members.size()).isEqualTo(2);
    }
}
