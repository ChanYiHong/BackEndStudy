package hcy.servlet.basic.web.frontcontroller.v5;

import hcy.servlet.basic.web.frontcontroller.ModelView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface MyHandlerAdapter {

    // 컨트롤러가 넘어왔을 때, 이 컨트롤러 (핸들러)를 지원할 수 있어?
    boolean supports(Object handler);

    ModelView handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws ServletException, IOException;

}
