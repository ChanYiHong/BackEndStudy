package hcy.servlet.basic.web.frontcontroller.v3;

import hcy.servlet.basic.web.frontcontroller.ModelView;
import hcy.servlet.basic.web.frontcontroller.MyView;
import hcy.servlet.basic.web.frontcontroller.v2.ControllerV2;
import hcy.servlet.basic.web.frontcontroller.v2.controller.MemberFormControllerV2;
import hcy.servlet.basic.web.frontcontroller.v2.controller.MemberListControllerV2;
import hcy.servlet.basic.web.frontcontroller.v2.controller.MemberSaveControllerV2;
import hcy.servlet.basic.web.frontcontroller.v3.controller.MemberFormControllerV3;
import hcy.servlet.basic.web.frontcontroller.v3.controller.MemberListControllerV3;
import hcy.servlet.basic.web.frontcontroller.v3.controller.MemberSaveControllerV3;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "frontControllerV3", urlPatterns = "/front-controller/v3/*")
public class FrontControllerV3 extends HttpServlet {

    private Map<String, ControllerV3> controllerMap = new HashMap<>();

    public FrontControllerV3() {
        controllerMap.put("/front-controller/v3/members/new-form", new MemberFormControllerV3());
        controllerMap.put("/front-controller/v3/members/save", new MemberSaveControllerV3());
        controllerMap.put("/front-controller/v3/members", new MemberListControllerV3());
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("FrontControllerServletV3.service");

        String requestURI = request.getRequestURI();

        ControllerV3 controller = controllerMap.get(requestURI);

        if(controller == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        Map<String, String> paramMap = createParamMap(request);

        ModelView mv = controller.process(paramMap);

        // ViewResolver 역할. 논리 주소를 물리 주소 이름으로 변환.
        String viewName = mv.getViewName();
        MyView view = viewResolver(viewName);

        view.render(mv.getModel(), request, response);
    }

    private MyView viewResolver(String viewName) {
        return new MyView("/WEB-INF/views/" + viewName + ".jsp");
    }

    private Map<String, String> createParamMap(HttpServletRequest request) {
        Map<String, String> paramMap = new HashMap<>();
        request.getParameterNames().asIterator()
                .forEachRemaining(paramName -> paramMap.put(paramName, request.getParameter(paramName)));
        return paramMap;
    }
}
