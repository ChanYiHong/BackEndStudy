package hcy.servlet.basic.request;

import org.springframework.util.StreamUtils;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@WebServlet(name = "requestBodyStringServlet", urlPatterns = "/request-body-string")
public class RequestBodyStringServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletInputStream inputStream = request.getInputStream(); // 메세지 바디의 내용을 바이트 코드로 받음.

        String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);// 바이트 코드를 String으로 변환 할 때 스프링이 제공하는 유틸리티 클래스 사용.
        // 항상 바이트를 문자로 변경할 때 , 혹은 문자를 바이트로 변경 할 때 인코딩 정보를 알려줘야 함. 보통 UTF_8

        System.out.println("messageBody = " + messageBody);

        response.getWriter().write("ok");
    }
}
