package hcy.servlet.web.springmvc.old;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * spring bean의 이름으로 handler (controller) 를 찾을 수 있는 handler mapping이 필요.
 * 그 후 Controller 인터페이스를 실행 할 수 있는 핸들러 어댑터를 찾고 실행해야 함.
 * 스프링 부트는 자동으로 핸들러 매핑과 핸들러 어댑터를 등록.
 */
@Component("/springmvc/old-controller") // 스프링 빈의 이름을 그냥 URL 패턴으로 맞춤.
public class OldController implements Controller {

    @Override
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        System.out.println("OldController.handleRequest");
        return new ModelAndView("new-form");
    }
}
