package hcy.exception.servlet;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Controller
public class ServletExController {

    // Controller -> Interceptor -> (Dispatcher) Servlet -> Filter -> WAS (여기까지 전파). WAS 기본 에러 페이지가 나옴.
    @GetMapping("/error-ex")
    public void errorEx() {
        throw new RuntimeException("예외 발생!");
    }

    // 호출한다고 당장 예외를 발생시키진 않고, 서블릿 컨테이너 (WAS) 한테 오류가 발생했다는 점을 전달할 수 있다.
    @GetMapping("/error-404")
    public void error404(HttpServletResponse response) throws IOException {
        response.sendError(404, "404 오류");
    }

    @GetMapping("/error-500")
    public void error500(HttpServletResponse response) throws IOException {
        response.sendError(500);
    }

}
