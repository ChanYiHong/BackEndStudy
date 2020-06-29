package jpabook.jpashop.controller;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    /**
     *
     * Model이란?
     * Model에다가 addAttribute("memberForm", new MemberForm());
     * Controller에서 view로 넘어갈 때 view로 이 데이터를 실어서 보낸다.
     */
    @GetMapping("/members/new")
    public String createForm(Model model){
        model.addAttribute("memberForm", new MemberForm());
        return "members/createMemberForm";
    }


    @PostMapping("/members/new")
    // Form에 있는 javax.validation의 기능을 감지해서 validation을 해줌
    public String create(@Valid MemberForm memberForm, BindingResult result) {

        // BindingResult. Spring 에서 제공. Valid해서 에러가 생기면 팅구지 않고 여기로 들어옴
        // Binding한 오류 valid까지 화면에 뿌려줌
        if (result.hasErrors()) {
            return "members/createMemberForm";
        }

        Address address = new Address(memberForm.getCity(), memberForm.getStreet(), memberForm.getZipcode());

        Member member = new Member();
        member.setName(memberForm.getName());
        member.setAddress(address);

        memberService.join(member);

        return "redirect:/";
    }


    @GetMapping("/members")
    public String list(Model model){
        // 원래 Member 객체를 Member detour나 화면에맞는 데이터 트랜스폼 오브젝트로 바꿔서 필요한 정보만 모델에 담아서 넘겨야 함
        // API를 만들 때는 "절대" entity를 웹으로 반환하면 안됨!! 중요
        // (이 예제는 아주 간단한 예제라 이렇게 했음.. )
        List<Member> members = memberService.findMembers();
        // key : "members"  value : members 객체
        model.addAttribute("members", members);
        // model에 담아서 화면에 넘긴다.
        return "members/memberList";
    }
}
