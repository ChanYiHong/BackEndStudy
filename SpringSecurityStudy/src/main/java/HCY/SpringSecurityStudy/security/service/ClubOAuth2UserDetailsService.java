package HCY.SpringSecurityStudy.security.service;

import HCY.SpringSecurityStudy.security.dto.ClubAuthMemberDTO;
import HCY.SpringSecurityStudy.entity.ClubMember;
import HCY.SpringSecurityStudy.entity.ClubMemberRole;
import HCY.SpringSecurityStudy.repository.ClubMemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class ClubOAuth2UserDetailsService extends DefaultOAuth2UserService {

    private final ClubMemberRepository clubMemberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        log.info("---------------------------------------------------");
        log.info("userRequest : " + userRequest);       // OAuth2UserRequest 객체.

        String clientName = userRequest.getClientRegistration().getClientName();

        log.info("---------------------------------------------------");
        log.info("clientName : " + clientName);         // Google 출력.
        log.info(userRequest.getAdditionalParameters());

        OAuth2User oAuth2User = super.loadUser(userRequest);

        log.info("===================================================");
        oAuth2User.getAttributes().forEach((k,v) -> {
            log.info(k + " : " + v);        // sub, picture, email, email_verified 존재.
        });

        String email = null;

        if(clientName.equals("Google")){
            email = oAuth2User.getAttribute("email");   // 구글일 경우 이메일 추출..
        }

        log.info("EMAIL: " + email);

        ClubMember member = saveSocialMember(email);

        //return oAuth2User;

        ClubAuthMemberDTO clubAuthMember = new ClubAuthMemberDTO(
                member.getEmail(),
                member.getPassword(),
                true,
                member.getRoleSet().stream().map(
                        role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
                .collect(Collectors.toList()),
                oAuth2User.getAttributes()
        );

        return clubAuthMember;

    }

    private ClubMember saveSocialMember(String email) {

        // 기존에 동일한 이메일로 가입한 회원이 있는 경우에는 그대로 조회..
        Optional<ClubMember> result = clubMemberRepository.findByEmail(email, true);
        if(result.isPresent()) {
            return result.get();
        }

        // 없다면 새로 만듬. (비밀번호 이슈는 나중에..)
        ClubMember clubMember = ClubMember.builder()
                .email(email)
                .password(passwordEncoder.encode("1111"))
                .fromSocial(true)
                .build();

        clubMember.addMemberRole(ClubMemberRole.USER);
        clubMemberRepository.save(clubMember);
        return clubMember;
    }
}
