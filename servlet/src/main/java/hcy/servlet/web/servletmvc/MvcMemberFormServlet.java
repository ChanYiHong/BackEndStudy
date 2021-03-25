package hcy.servlet.web.servletmvc;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// Controller 역할.
// MVC 패턴에서는 항상 컨트롤러를 거쳐서 View로 이동. JSP로 가주면 됨.
@WebServlet(name = "mvcMemberFormServlet", urlPatterns = "/servlet-mvc/members/new-form")
public class MvcMemberFormServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String viewPath = "/WEB-INF/views/new-form.jsp";

        RequestDispatcher dispatcher = request.getRequestDispatcher(viewPath); // Controller 에서 View로 이동할 때 사용.
        dispatcher.forward(request, response); // 이걸 호출하면 서블릿에서 jsp를 호출 가능.
        // 다른 서블릿이나 JSP로 이동할 수 있는 기능. 서버 내부에서 다시 호출 발생.


    }
}
