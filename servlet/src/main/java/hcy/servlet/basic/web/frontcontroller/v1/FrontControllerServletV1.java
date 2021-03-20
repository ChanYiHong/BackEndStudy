package hcy.servlet.basic.web.frontcontroller.v1;

import hcy.servlet.basic.web.frontcontroller.v1.controller.MemberFormControllerV1;
import hcy.servlet.basic.web.frontcontroller.v1.controller.MemberListControllerV1;
import hcy.servlet.basic.web.frontcontroller.v1.controller.MemberSaveControllerV1;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

// /front-controller/v1 하위의 어떤 경로가 들어와도 이 서블릿이 무조건 호출된다.
@WebServlet(name = "frontControllerServiceV1", urlPatterns = "/front-controller/v1/* ")
public class FrontControllerServletV1 extends HttpServlet {

    // 어떤 URL이 들어오면 어떤 Controller를 호출 할 것인지 매핑하기 위한 정보.
    private Map<String, ControllerV1> controllerMap = new HashMap<>();

    public FrontControllerServletV1() {
        controllerMap.put("/front-controller/v1/members/new-form", new MemberFormControllerV1());
        controllerMap.put("/front-controller/v1/members/save", new MemberSaveControllerV1());
        controllerMap.put("/front-controller/v1/members", new MemberListControllerV1());
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("FrontControllerServletV1.service");

        String requestURI = request.getRequestURI();
        ControllerV1 controller = controllerMap.get(requestURI);

        if(controller == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        controller.process(request, response);
    }
}
