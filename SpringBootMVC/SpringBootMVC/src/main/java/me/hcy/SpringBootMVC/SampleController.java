package me.hcy.SpringBootMVC;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SampleController {

    @GetMapping("/hellooo")
    public String hello(Model model){
        model.addAttribute("name", "chanyi");
        // 여기서 리턴하는 hello는 응답의 본문이 아님
        // rest controller가 아니라 그냥 controller이기 때문이다
        return "hello";
    }
}

/**

 이 hello 라는 view에다가 전달해야 하는 Model 정보들, 어떤 데이터들은
 Model 에다가 저장 할 수 있음

 Map이라고 생각하고 담으시면 됩니다

 **/

