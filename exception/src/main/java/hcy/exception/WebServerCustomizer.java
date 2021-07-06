package hcy.exception;

import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

//@Component
public class WebServerCustomizer implements WebServerFactoryCustomizer<ConfigurableWebServerFactory> {

    @Override
    public void customize(ConfigurableWebServerFactory factory) {

        // 서버 내부에서 오류가 발생하면 WAS가 다시 컨트롤러까지 해당 경로를 호출해줌. 그래서 이 경로를 처리할 컨트롤러가 필요함.
        // 마치 http 요청이 다시온거마냥.. 물론 실제로 요청이 온건 아님. 내부에서 일어나는 일.
        // 그리고 WAS는 어떤 오류 정보들이 있는제 request의 attribute에 추가해서 넘겨준다.
        ErrorPage errorPage404 = new ErrorPage(HttpStatus.NOT_FOUND, "/error-page/404");
        ErrorPage errorPage500 = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/error-page/500");
        ErrorPage errorPageEx = new ErrorPage(RuntimeException.class, "/error-page/500");

        factory.addErrorPages(errorPage404, errorPage500, errorPageEx);

    }
}
