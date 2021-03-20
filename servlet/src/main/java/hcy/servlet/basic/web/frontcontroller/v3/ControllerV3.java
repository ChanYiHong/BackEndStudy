package hcy.servlet.basic.web.frontcontroller.v3;

import hcy.servlet.basic.web.frontcontroller.ModelView;

import java.util.Map;

public interface ControllerV3 {

    // 서블릿 기술이 안들어감. 서블릿에 종속적이지 않음. 테스트도 쉬워짐. 유지보수도 쉬워짐.
    ModelView process(Map<String, String> paramMap);

}
