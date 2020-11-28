package study.datajpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.entity.Member;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
//JPA의 모든 데이터 변경은 트렌젝션 안에서 이루어져야함.
@Transactional
class MemberJpaRepositoryTest {

    @Autowired MemberJpaRepository memberJpaRepository;

    @Test
    public void testMember() {
        Member member = new Member("user1");
        Member saveMember = memberJpaRepository.save(member);
        Member findMember = memberJpaRepository.find(saveMember.getId());
        assertThat(saveMember).isEqualTo(findMember);
    }


    @Test
    public void basicCRUD() {
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");
        memberJpaRepository.save(member1);
        memberJpaRepository.save(member2);

        // 단건 조회 검증.
        Member findMember1 = memberJpaRepository.findById(member1.getId()).get();
        Member findMember2 = memberJpaRepository.findById(member2.getId()).get();

        assertThat(findMember1).isEqualTo(member1);
        assertThat(findMember2).isEqualTo(member2);

        // 리스트 조회 검증.
        List<Member> all = memberJpaRepository.findAll();
        assertThat(all.size()).isEqualTo(2);

        // 카운트 검증.
        Long count = memberJpaRepository.count();
        assertThat(count).isEqualTo(2);

        // 삭제 검증.
        memberJpaRepository.delete(member1);
        memberJpaRepository.delete(member2);

        List<Member> all2 = memberJpaRepository.findAll();
        assertThat(all2.size()).isEqualTo(0);
    }

    @Test
    public void findByUsernameAndAgeGreaterThanTest(){
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("AAA", 20);
        memberJpaRepository.save(m1);
        memberJpaRepository.save(m2);

        List<Member> result = memberJpaRepository.findByUsernameAndAgeGreaterThan("AAA", 15);

        assertThat(result.get(0).getAge()).isEqualTo(20);
        assertThat(result.get(0).getUsername()).isEqualTo("AAA");
        assertThat(result.size()).isEqualTo(1);
    }


    @Test
    public void paging(){
        //given
        memberJpaRepository.save(new Member("username1", 10));
        memberJpaRepository.save(new Member("username2", 10));
        memberJpaRepository.save(new Member("username3", 10));
        memberJpaRepository.save(new Member("username4", 10));
        memberJpaRepository.save(new Member("username5", 10));

        int age = 10;
        int offset = 0;
        int limit = 3;

        //when
        List<Member> members = memberJpaRepository.findByPage(age, offset, limit);
        long totalCount = memberJpaRepository.totalCount(age);

        // 페이지 계산 공식 적용... totalPage = totalCount / size ...
        // 마지막 페이지 ... 최초 페이지 ...

        //then
        assertThat(members.size()).isEqualTo(3);
        assertThat(totalCount).isEqualTo(5);

    }


    @Test
    public void bulkUpdate() {
        // given
        memberJpaRepository.save(new Member("member1", 10));
        memberJpaRepository.save(new Member("member2", 20));
        memberJpaRepository.save(new Member("member3", 30));
        memberJpaRepository.save(new Member("member4", 40));
        memberJpaRepository.save(new Member("member5", 50));

        // when
        int resultCount = memberJpaRepository.bulkAgePlus(20);

        // then
        assertThat(resultCount).isEqualTo(4);
    }

}