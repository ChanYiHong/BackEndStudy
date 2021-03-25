package hcy.servlet.web.frontcontroller.v3.controller;

import hcy.servlet.web.frontcontroller.ModelView;
import hcy.servlet.web.frontcontroller.v3.ControllerV3;
import hcy.servlet.domain.member.Member;
import hcy.servlet.domain.member.MemberRepository;

import java.util.List;
import java.util.Map;

public class MemberListControllerV3 implements ControllerV3 {

    MemberRepository memberRepository = MemberRepository.getInstance();

    @Override
    public ModelView process(Map<String, String> paramMap) {
        List<Member> members = memberRepository.findAll();
        ModelView mv = new ModelView("members");

        mv.getModel().put("members", members);
        return mv;
    }
}
