package HCY.SpringSecurityStudy.repository;

import HCY.SpringSecurityStudy.entity.ClubMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClubMemberRepository extends JpaRepository<ClubMember, Long> {
}
