package study.datajpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;

import javax.persistence.EntityManager;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberRepositoryTest {

    @Autowired MemberRepository memberRepository;
    @Autowired TeamRepository teamRepository;
    @Autowired EntityManager em;

    @Test
    public void testMember() {
        Member member = new Member("user1");
        Member saveMember = memberRepository.save(member);
        // 있을 수도 있고, 없을 수도 있어서 Optional로 제공.
        Member findMember = memberRepository.findById(saveMember.getId()).get();

        assertThat(saveMember).isEqualTo(findMember);
    }

    @Test
    public void basicCRUD() {
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");
        memberRepository.save(member1);
        memberRepository.save(member2);

        // 단건 조회 검증.
        Member findMember1 = memberRepository.findById(member1.getId()).get();
        Member findMember2 = memberRepository.findById(member2.getId()).get();

        assertThat(findMember1).isEqualTo(member1);
        assertThat(findMember2).isEqualTo(member2);

        // 리스트 조회 검증.
        List<Member> all = memberRepository.findAll();
        assertThat(all.size()).isEqualTo(2);

        // 카운트 검증.
        Long count = memberRepository.count();
        assertThat(count).isEqualTo(2);

        // 삭제 검증.
        memberRepository.delete(member1);
        memberRepository.delete(member2);

        List<Member> all2 = memberRepository.findAll();
        assertThat(all2.size()).isEqualTo(0);
    }


    @Test
    public void findByUsernameAndAgeGreaterThanTest(){
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("AAA", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findByUsernameAndAgeGreaterThan("AAA", 15);

        assertThat(result.get(0).getAge()).isEqualTo(20);
        assertThat(result.get(0).getUsername()).isEqualTo("AAA");
        assertThat(result.size()).isEqualTo(1);
    }

    @Test
    public void findUserQueryAnnotationTest(){
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("AAA", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findUser("AAA", 10);

        assertThat(result.get(0).getAge()).isEqualTo(10);
        assertThat(result.get(0).getUsername()).isEqualTo("AAA");
        assertThat(result.size()).isEqualTo(1);
    }

    @Test
    public void findUsernameListTest(){
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<String> usernameList = memberRepository.findUsernameList();

        assertThat(usernameList.get(0)).isEqualTo("AAA");
        assertThat(usernameList.get(1)).isEqualTo("BBB");
    }

    @Test
    public void findMemberDtoTest(){

        Team team = new Team("Team1");
        teamRepository.save(team);
        Member m1 = new Member("AAA", 10);
        m1.changeTeam(team);
        memberRepository.save(m1);

        List<MemberDto> memberDtos = memberRepository.findMemberDto();

        assertThat(memberDtos.get(0).getUsername()).isEqualTo("AAA");
        assertThat(memberDtos.get(0).getTeamName()).isEqualTo("Team1");
    }

    @Test
    public void findByNamesTest(){
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> names = memberRepository.findByNames(Arrays.asList("AAA", "BBB"));

        assertThat(names.get(0).getUsername()).isEqualTo("AAA");
        assertThat(names.get(1).getAge()).isEqualTo(20);
    }


    @Test
    public void returnTypetest(){
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> aaa = memberRepository.findListByUsername("AAA");
        Member findMember = memberRepository.findMemberByUsername("AAA");
        Optional<Member> optionalMember = memberRepository.findOptionalByUsername("AAA");

        assertThat(optionalMember.get()).isEqualTo(m1);
    }


    @Test
    public void pagingPage(){
        //given
        memberRepository.save(new Member("username1", 10));
        memberRepository.save(new Member("username2", 10));
        memberRepository.save(new Member("username3", 10));
        memberRepository.save(new Member("username4", 10));
        memberRepository.save(new Member("username5", 10));

        int age = 10;
        // 0페이지 부터 3개... 사용자 이름으로 내림차순으로..
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));

        // when..
        Page<Member> page = memberRepository.findByAge(age, pageRequest);

        // 외부 API 로 내보낼 수 있음. 엔티티를 DTO로 꼭 변환하자!
        Page<MemberDto> toMap = page.map(member -> new MemberDto(member.getId(), member.getUsername(), null));

        // then  total count 까지 가져옴
        List<Member> content = page.getContent();
        long totalElements = page.getTotalElements();

        for (Member member : content) {
            System.out.println("member = " + member);
        }

        System.out.println("totalElement = " + totalElements);

        assertThat(content.size()).isEqualTo(3);
        assertThat(page.getTotalElements()).isEqualTo(5);
        assertThat(page.getNumber()).isEqualTo(0);
        assertThat(page.getTotalPages()).isEqualTo(2);
        assertThat(page.isFirst()).isTrue();
        assertThat(page.hasNext()).isTrue();
    }


    // 0번 째에 3개를 가져오라 하면, slice는 limit +1 인 4개를 요청.
    @Test
    public void pagingSlice(){
        //given
        memberRepository.save(new Member("username1", 10));
        memberRepository.save(new Member("username2", 10));
        memberRepository.save(new Member("username3", 10));
        memberRepository.save(new Member("username4", 10));
        memberRepository.save(new Member("username5", 10));

        int age = 10;
        // 0페이지 부터 3개... 사용자 이름으로 내림차순으로..
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));

        // when..
        Slice<Member> page = memberRepository.findByAge(age, pageRequest);

        // then  total count 까지 가져옴
        List<Member> content = page.getContent();
//        long totalElements = page.getTotalElements();

        for (Member member : content) {
            System.out.println("member = " + member);
        }

//        System.out.println("totalElement = " + totalElements);

        assertThat(content.size()).isEqualTo(3);
//        assertThat(page.getTotalElements()).isEqualTo(5);
        assertThat(page.getNumber()).isEqualTo(0);
//        assertThat(page.getTotalPages()).isEqualTo(2);
        assertThat(page.isFirst()).isTrue();
        assertThat(page.hasNext()).isTrue();
    }

    @Test
    public void bulkUpdate() {
        // given
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 20));
        memberRepository.save(new Member("member3", 30));
        memberRepository.save(new Member("member4", 40));
        memberRepository.save(new Member("member5", 50));

        // when
        int resultCount = memberRepository.bulkAgePlus(20);

        //em.clear();

        Member member5 = memberRepository.findMemberByUsername("member5");
        System.out.println(member5.getAge());

        // then
        assertThat(resultCount).isEqualTo(4);
    }

    @Test
    public void findMemberLazy(){
        //given
        //member1 -> teamA
        //member2 -> teamB

        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        teamRepository.save(teamA);
        teamRepository.save(teamB);
        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 20, teamB);
        memberRepository.save(member1);
        memberRepository.save(member2);

        em.flush();
        em.clear();

        //when

        List<Member> members = memberRepository.findAll();

        for (Member member : members) {
            System.out.println(member.getUsername());
            System.out.println(member.getTeam().getName());
        }

        List<Member> memberFetches = memberRepository.findMemberFetchJoin();

        for (Member member : memberFetches) {
            System.out.println(member.getUsername());
            System.out.println(member.getTeam().getName());
        }

    }
}