package hcy.springmvc.basic.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import hcy.springmvc.basic.HelloData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * {"username":"hello", "age":20}
 * content-type: application/json
 */

@Slf4j
@Controller
public class RequestBodyJsonController {

    private ObjectMapper objectMapper = new ObjectMapper();

    @PostMapping("/request-body-json-v1")
    public void requestBodyJsonV1(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ServletInputStream inputStream = request.getInputStream();
        String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);

        log.info("messageBody={}", messageBody);
        HelloData helloData = objectMapper.readValue(messageBody, HelloData.class);
        log.info("username={}, age={}", helloData.getUsername(), helloData.getAge());

        response.getWriter().write("ok");
    }

    @ResponseBody
    @PostMapping("/request-body-json-v2")
    public String requestBodyJsonV2(@RequestBody String messageBody) throws IOException {

        log.info("messageBody={}", messageBody);
        HelloData helloData = objectMapper.readValue(messageBody, HelloData.class);
        log.info("username={}, age={}", helloData.getUsername(), helloData.getAge());

        return "ok";
    }


    // Object Mapper 안쓰고 간편하게 @RequestBody로 해결!. -> HttpEntity, @RequestBody를 사용하면 HTTP 메세지 컨버터가 HTTP 메세지 바디의 내용을 우리가 원하는 문자나 객체 등으로 변환해준다.
    // @RequestBody는 생략하면 안됨. 생략하면 @ModelAttribute처럼 되어버림.
    @ResponseBody
    @PostMapping("/request-body-json-v3")
    public String requestBodyJsonV3(@RequestBody HelloData helloData) {

        log.info("username={}, age={}", helloData.getUsername(), helloData.getAge());

        return "ok";
    }


    @ResponseBody
    @PostMapping("/request-body-json-v4")
    public String requestBodyJsonV4(HttpEntity<HelloData> data) {

        HelloData body = data.getBody();
        log.info("username={}, age={}", body.getUsername(), body.getAge());

        return "ok";
    }

    // ResponseBody를 사용하면 해당 객체를 HTTP 메세지 바디에 직접 넣어줄 수 있다. (물론 HttpEntity사용 가능)
    // @RequestBody 요청 : JSON 요청 -> HTTP 메세지 컨버터 -> 객체
    // @ResponseBody 응답 : 객체 -> HTTP 메세지 컨버터 -> JSON 응답
    @ResponseBody
    @PostMapping("/request-body-json-v5")
    public HelloData requestBodyJsonV5(@RequestBody HelloData data) {

        log.info("username={}, age={}", data.getUsername(), data.getAge());

        return data;
    }

}
