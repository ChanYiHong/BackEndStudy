package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
// JPA의 데이터 변경과 로직들은 transaction안에서 실행이 되어야 함. (그래야 레이즈 로딩 이런게 다 됨?)
// 데이터 베이스를 "읽기"만 하는 로직들은 readOnly = true 를 적용하면 성능최적화가 된다.
@Transactional(readOnly = true)
public class MemberService {

    /**
     * 생성자 injection이 젤 좋음. 요즘
     * final 을 권장. 주입 후 변경할 일이 없음.
     */
    private final MemberRepository memberRepository;

    @Autowired
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    /**
     * 회원가입
     */
    //따로 Transactional annotation을 붙이면 default가 readOnly가 false임
    @Transactional
    public Long join(Member member) {
        validateDuplicateMember(member);    // 중복 회원 검증.
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        // EXCEPTION
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if(!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }

    }
    // 회원 전체 조회
    public List<Member> findMembers()  {
        return memberRepository.findAll();
    }

    // 회원 한건 조회
    public Member findOne(Long memberId) {
        return memberRepository.findOne(memberId);
    }

    @Transactional
    public void update(Long id, String name){
        Member member = memberRepository.findOne(id);
        member.setName(name);
    }
}
