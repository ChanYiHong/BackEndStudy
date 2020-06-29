package jpabook.jpashop.controller;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter @Setter
public class MemberForm {

    @NotEmpty(message = "회원 이름은 필수 입니다!!!")
    private String name;

    private String city;
    private String street;
    private String zipcode;

}

/**
 *
 * 엔티티말고 따로 이렇게 Form 객체를 만들어서
 * 엔티티는 단순하게 유지하는 것이 좋다!
 *
 */
