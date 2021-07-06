package hcy.exception.resolver;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 *  ExceptionResolver 활용
 *  1. 예외 상태 코드 변환
 *  2. 뷰 템플릿 처리
 *  3. API 응답 처리 
 */
@Slf4j
public class MyHandlerExceptionResolver implements HandlerExceptionResolver {

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {

        // 예외를 꿀꺽 삼켜버림.
        try {
            // IllegalArgumentException은 400에러로 치환. (500 -> 400)
            if (ex instanceof IllegalStateException) {
                log.info("IllegalArgumentException resolver to 400");
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, ex.getMessage());
                return new ModelAndView(); // 빈 깡통 ModelAndView를 반환하면 뷰를 렌더링 하지 않고, 정상 흐름으로 서블릿이 리턴.
                // 그치만 여기선 sendError를 했기 때문에 오류가 있음을 파악하고 400 에러화면을 띄움.
                // 여튼 여기서 익셉션을 꿀꺽 먹고 다른걸로 바꾼다는게 중요함.
            }
        } catch (IOException e) {
            log.error("resolver ex", e);
        }

        return null; // null을 반환하면, 다른 ExceptionResolver를 찾고, 그것도 없으면 예외처리가 안되고, 기존에 발생한 예외를 서블릿 밖으로 던진다.

    }
}
