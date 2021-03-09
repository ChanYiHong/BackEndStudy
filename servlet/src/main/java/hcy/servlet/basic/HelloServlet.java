package hcy.servlet.basic;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// http 요청이 오면 WAS(Servlet Container)가 http 요청 메세지를 기반으로 Request, Response 객체를 만들어서 Servlet에 던져준다.
// /hello 를 호출하면 웹브라우저가 http 요청 메세지를 만들어서 서버에 던짐.
// 응답에 대한 response 객체를 서버가 만들어서 보내줍니다.
// 서블릿 객체는 싱글톤.
@WebServlet(name = "helloServlet", urlPatterns = "/hello")
public class HelloServlet extends HttpServlet {

    // 서블릿이 호출되면 service 메소드가 호출된다.
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("HelloServlet.service");

        System.out.println(request);
        System.out.println(response);

        String name = request.getParameter("name");
        System.out.println(request.getParameter("name"));

        response.setContentType("text/plain");
        response.setCharacterEncoding("utf-8");
        response.getWriter().write("hello " + name); // http 메세지 바디에 데이터가 들어감.



    }
}
