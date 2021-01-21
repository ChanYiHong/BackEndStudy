package HCY.SpringSecurityStudy.security;

import HCY.SpringSecurityStudy.entity.ClubMember;
import HCY.SpringSecurityStudy.entity.ClubMemberRole;
import HCY.SpringSecurityStudy.repository.ClubMemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.stream.IntStream;

@SpringBootTest
public class ClubMemberTests {

    @Autowired
    private ClubMemberRepository clubMemberRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void insertDummies() throws Exception {

        IntStream.rangeClosed(1, 100).forEach(i -> {
            ClubMember clubMember = ClubMember.builder()
                    .email("user" + i + "@aaa.com")
                    .name("user" + i)
                    .fromSocial(false)
                    .password(passwordEncoder.encode("1111"))
                    .build();

            clubMember.addMemberRole(ClubMemberRole.USER);
            if(i > 80) clubMember.addMemberRole(ClubMemberRole.MANAGER);
            if(i > 90) clubMember.addMemberRole(ClubMemberRole.ADMIN);

            clubMemberRepository.save(clubMember);
        });
    }
}
