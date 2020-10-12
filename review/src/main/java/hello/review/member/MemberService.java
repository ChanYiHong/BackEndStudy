package hello.review.member;

public interface MemberService {

    void join(Member member);
    Member findMember(Long memberId);
}
