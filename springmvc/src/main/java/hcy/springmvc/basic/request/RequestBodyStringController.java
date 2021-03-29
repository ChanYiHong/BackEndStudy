package hcy.springmvc.basic.request;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

@Slf4j
@Controller
public class RequestBodyStringController {

    @PostMapping("/request-body-string-v1")
    public void requestBodyString(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ServletInputStream inputStream = request.getInputStream();
        String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);

        log.info("messageBody={}", messageBody);

        response.getWriter().write("ok");
    }

    // 서블릿 request, response가 통으로 필요하지 않기 때문에 InputStream과 OutputStream만 따로 받을 수 있다.
    @PostMapping("/request-body-string-v2")
    public void requestBodyStringV2(InputStream inputStream, Writer responseWriter) throws IOException {
        String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);

        log.info("messageBody={}", messageBody);
        responseWriter.write("ok");
    }


    // 마치 HTTP 메세지를 그대로 주고 받는 것 처럼. 스프링이 알아서 inputStream을 해줌.
    // HttpEntity 객체는 http 헤더와 바디 정보를 편리하게 조회하는 객체.
    // HttpEntity<String> 이라고 하면 알아서 바이트 코드를 String으로 인코딩 해줌.
    // 단, 요청 파라미터와는 아무 관계가 없음. (쿼리 스트링 <Get방식, Form 방식> 과는 아무 관계 없음 중요!)
    @PostMapping("/request-body-string-v3")
    public HttpEntity<String> requestBodyStringV3(HttpEntity<String> httpEntity) throws IOException {

        log.info("messageBody={}", httpEntity.getBody());
        // httpEntity.getHeaders();
        return new HttpEntity<>("ok");
    }

    // ResponseEntity에는 상태 코드를 넣을 수 있음.
    // RequestEntity는 URL 등의 조회를 더 할 수 있음. 추가 기능이라고 보면 될듯.
    // 이 둘은 스프링 MVC 내부에서 HTTP 메세지 바디를 읽어서 문자나 객체로 변환해서 전달해 주는데,
    // 이 때 HTTP 메세지 컨버터 (HttpMessageConverter) 라는 기능을 사용한다.
    @PostMapping("/request-body-string-v3_2")
    public ResponseEntity<String> requestBodyStringV3_2(RequestEntity<String> requestEntity) throws IOException {

        log.info("messageBody={}", requestEntity.getBody());
        //requestEntity.getHeaders();
        //requestEntity.getUrl();

        return new ResponseEntity<>("ok", HttpStatus.CREATED);
    }

    // HttpEntity 쓰기도 귀찮다?
    // 그럼 @RequestBody 사용!
    @ResponseBody // 앞서 했듯이 Controller 어노테이션에서 이걸 쓰면 응답 메세지에 return 값을 박아서 보냄.
    @PostMapping("/request-body-string-v4")
    public String requestBodyStringV4(@RequestBody String messageBody) throws IOException {

        log.info("messageBody={}", messageBody);

        return "ok";
    }

}
