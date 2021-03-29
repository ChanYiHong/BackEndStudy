package hcy.springmvc.basic.request;

import hcy.springmvc.basic.HelloData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Controller
@Slf4j
public class RequestParamController {

    @RequestMapping("/request-param-v1")
    public void requestParamV1(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = request.getParameter("username");
        int age = Integer.parseInt(request.getParameter("age"));

        log.info("username = {}, age = {}", username, age);

        response.getWriter().write("ok");
    }

    @ResponseBody // ok 라는 문자를 http 응답 메세지에 그대로 박아서 응답. Controller 어노테이션이지만 view 조회를 안함. 마치 @RestController 어노테이션 같음 ㅇㅇ.
    @RequestMapping("/request-param-v2")
    public String requestParamV2(
            @RequestParam("username") String memberName,
            @RequestParam("age") int memberAge
    ) {
        log.info("username = {}, age = {}", memberName, memberAge);

        return "ok";
    }

    @ResponseBody
    @RequestMapping("/request-param-v3")
    public String requestParamV3(
            @RequestParam String username, // 요청 파라미터 이름을 맞추면 () 생략가능.
            @RequestParam int age
    ) {
        log.info("username = {}, age = {}", username, age);

        return "ok";
    }

    @ResponseBody
    @RequestMapping("/request-param-v4")
    public String requestParamV4(String username, int age) { // @RequestParam까지도 생략 가능.
        log.info("username = {}, age = {}", username, age);

        return "ok";
    }

    @ResponseBody
    @RequestMapping("/request-param-required")
    public String requestParamRequired(@RequestParam(required = true) String username, // 요청 파라미터가 꼭 안들어와도 됨. 빈문자가 그대로 받아지는 문제점..
                                       @RequestParam(required = false) Integer age) { // 요청 파라미터가 무조건 들어와야 해. (디폴트임) int는 null이 못들어가서 Integer로.
        log.info("username = {}, age = {}", username, age);

        return "ok";
    }

    // 중요.
    @ResponseBody
    @RequestMapping("/request-param-default")
    public String requestParamDefault(@RequestParam(required = true, defaultValue = "guest") String username, // 요청 파라미터가 꼭 안들어와도 됨. 만약 빈 문자열도 값이 없다 처리되고 디폴트벨류로 됨.
                                       @RequestParam(required = false, defaultValue = "-1") int age) { // 요청 파라미터가 무조건 들어와야 해. (디폴트임) 이떄는 int 사용 가능. 값이 없을 경우 디폴트 벨류 값이 들어감.
        log.info("username = {}, age = {}", username, age);

        return "ok";
    }

    @ResponseBody
    @RequestMapping("/request-param-map")
    public String requestParamMap(@RequestParam Map<String, Object> paramMap) { // 요청 파라미터 한꺼번에 꺼내기. 보통 1개 씀. 맵 써도 무방해요.
        log.info("username = {}, age = {}", paramMap.get("username"), paramMap.get("age"));

        return "ok";
    }

    @ResponseBody
    @RequestMapping("/request-param-mapV2")
    public String requestParamMapV2(@RequestParam MultiValueMap<String, Object> paramMap) { // 파라미터가 한개가 확실하지 않으면 멀티벨류맵 쓰세요.
        log.info("username = {}, age = {}", paramMap.get("username"), paramMap.get("age"));

        return "ok";
    }

    /**
     * HelloData 객체 생성,
     * 요청 파라미터의 이름으로 HelloData 객체의 프로퍼티를 찾음.
     * 그리고 해당 프로퍼티의 setter를 호출해서 파라미터의 값을 입력 (바인딩) 한다.
     *
     */
    @ResponseBody
    @RequestMapping("/model-attribute-v1")
    public String modelAttributeV1(@ModelAttribute HelloData helloData) {

        log.info("helloData = {}", helloData);

        return "ok";
    }

    @ResponseBody
    @RequestMapping("/model-attribute-v2")
    public String modelAttributeV2(HelloData helloData) { // @ModelAttribute 생략 가능..

        log.info("helloData = {}", helloData);

        return "ok";
    }

}
