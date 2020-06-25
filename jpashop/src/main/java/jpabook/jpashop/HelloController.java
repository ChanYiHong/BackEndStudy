package jpabook.jpashop;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HelloController {

    @GetMapping("hello")
    // Model에다가 데이터를 실어서 Controller에서 View로 넘길 수 있음.
    public String hello(Model model) {
        // name이 데이터라는 key값을 hello로 해서 넘길거고.
        model.addAttribute("data", "hello!!!");
        // 화면 이름. templates 폴더 안에 hello.html을 뜻함
        return "hello";
    }
}
