package hello.review.beanfind;

import hello.review.AppConfig;
import hello.review.discount.DiscountPolicy;
import hello.review.member.MemberRepository;
import hello.review.member.MemoryMemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.NoUniqueBeanDefinitionException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Executable;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class ApplicationContextSameBeanFindTest {

    AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(SameBeanConfig.class);

    @Test
    @DisplayName("타입으로 조회시 같은 타입이 둘 이상 있으면 중복 오류가 발생한다.")
    public void findBeanByTypeDuplicate() throws Exception {
        //given
//        MemberRepository bean = ac.getBean(MemberRepository.class);
        //when

        //then
        assertThrows(NoUniqueBeanDefinitionException.class,
                () -> ac.getBean(MemberRepository.class));
    }

    @Test
    @DisplayName("타입으로 조회시 같은 타입이 둘 이상 있으면 빈 이름을 지정해야 한다,.")
    public void findBeanByName() throws Exception {
        //given
        MemberRepository bean = ac.getBean("memberRepository1",MemberRepository.class);
        //when

        //then
        assertThat(bean).isInstanceOf(MemberRepository.class);
    }

    @Test
    @DisplayName("특정 타입을 모두 조회하기")
    public void findAllbBeanByType() throws Exception {

        //given
        Map<String, MemberRepository> beansOfType = ac.getBeansOfType(MemberRepository.class);
        //when

        //then
        for(String key : beansOfType.keySet()){
            System.out.println("key = " + key + " value = " + beansOfType.get(key));
        }
        System.out.println("beansOfType = " + beansOfType);
        assertThat(beansOfType.size()).isEqualTo(2);

    }

    @Configuration
    static class SameBeanConfig {

        @Bean
        public MemberRepository memberRepository1(){
            return new MemoryMemberRepository();
        }

        @Bean
        public MemberRepository memberRepository2(){
            return new MemoryMemberRepository();
        }
    }
}
