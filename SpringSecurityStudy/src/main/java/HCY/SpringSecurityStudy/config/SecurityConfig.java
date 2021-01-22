package HCY.SpringSecurityStudy.config;

import HCY.SpringSecurityStudy.handler.ClubLoginSuccessHandler;
import HCY.SpringSecurityStudy.service.ClubUserDetailsService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@Log4j2
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    ClubUserDetailsService clubUserDetailsService;

    @Bean
    PasswordEncoder passwordEncoder () {
        return new BCryptPasswordEncoder();
    }

//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//
//        // 사용자 계정 : user1
//        auth.inMemoryAuthentication().withUser("user1")
//        // 1111 패스워드 인코딩 결과
//        .password("$2a$10$Rb/MB.f4K7btEgH73k8aj.MZc20y5Fsq1AhTU.1aDi1n3jXH8LNMS")
//                .roles("USER");
//    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

//        http.authorizeRequests()
//                .antMatchers("/sample/all").permitAll()
//                .antMatchers("/sample/member").hasRole("USER");

        http.formLogin();
        http.csrf().disable(); //csrf 토큰 미발행.

        http.oauth2Login().successHandler(successHandler());
        http.rememberMe().tokenValiditySeconds(60 * 60 * 24 * 7).userDetailsService(clubUserDetailsService);
    }

    @Bean
    public ClubLoginSuccessHandler successHandler() {
        return new ClubLoginSuccessHandler(passwordEncoder());
    }
}
