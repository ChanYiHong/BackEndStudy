package hello.hellospring.service;

import hello.hellospring.domain.Member;
import hello.hellospring.repository.MemberRepository;
import hello.hellospring.repository.MemoryMemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


public class MemberService {

    private final MemberRepository memberRepository;


    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    /** 회원가입 **/

    public Long join(Member member){

        // 같은 이름이 있는 중복 회원 x
        validateDuplicateMember(member);

        memberRepository.save(member);
        return member.getId();
    }

    // Extract method : command + option + m
    private void validateDuplicateMember(Member member) {
        Optional<Member> result = memberRepository.findByName(member.getName());
        // Optional 안에 감싸놔서 여러 메소드 사용 가능
        // 원래는 if == null 뭐 이렇게 해야하는데
        // ifPresent로 result에 뭐가 있으면 중복된 회원이 있는거니까...
        result.ifPresent(m -> {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        });
        // orElseGet : 값이 있으면 꺼내고, 없으면 뭘 해라 조건 설정 가능..
    }


    /** 전체 회원 조회 **/
    public List<Member> findMembers() {

        return memberRepository.findAll();
    }

    public Optional<Member> findOne(Long memberId){
        return memberRepository.findById(memberId);
    }
}
