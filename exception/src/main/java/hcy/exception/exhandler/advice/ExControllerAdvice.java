package hcy.exception.exhandler.advice;

import hcy.exception.api.ApiExceptionV2Controller;
import hcy.exception.exception.UserException;
import hcy.exception.exhandler.ErrorResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
//@RestControllerAdvice(annotations = RestController.class) // RestController 인 컨트롤러만
//@RestControllerAdvice("org.example.controller") // 패키지 별로 그안의 컨트롤러만
@RestControllerAdvice(assignableTypes = {ApiExceptionV2Controller.class}) // 특정 컨트롤러 지정. 부모 클래스 선택하면 자식까지 가능.
public class ExControllerAdvice {

    /** 이 컨트롤러에서 IllegalArguemtnException이 발생하면 ExceptionResolver에게 예외 처리 시도를 함
     * 처음 물어보는 애는 ExceptionHandlerExceptionResolver임.
     * @ExceptionHandler 가 있는 메소드가 있으면 얘를 호출해줌.
     * @RestController 라서 JSON으로 반환.
     * 정상 흐름으로 바꿔서 정상적으로 리턴해줌. HTTP 상태코드가 200이 됨..
     * 예외 상태코드까지도 바꾸려면 @ResponseStatus(HttpStatus.BAD_REQUEST) 이거 쓰면 됨.
     * 이러면 지저분하게 서블릿 컨테이너까지 안가서 지저분한 흐름이 안생김.
     *
     * 처리는 마치 일반 컨트롤러처럼 된다 신기.
     * */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ErrorResult illegalExHandler(IllegalArgumentException e) {
        log.error("[exceptionHandler ex]", e);
        return new ErrorResult("BAD", e.getMessage());
    }

    /** 다른 방법 **/
    @ExceptionHandler
    public ResponseEntity<ErrorResult> userExHandler(UserException e) {
        log.error("[exceptionHandler] ex", e);
        ErrorResult errorResult = new ErrorResult("USER-EX", e.getMessage());
        return new ResponseEntity<>(errorResult, HttpStatus.BAD_REQUEST);
    }

    /** Exception의 자식까지 다 포함. 즉 실수로 놓친 예외들 공통으로 처리!
     *  스프링의 우선순위는 항상 자세한 것이 우선순위를 가진다.
     * **/
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler
    public ErrorResult exHandler(Exception e) {
        log.error("[exceptionHandler] ex", e);
        return new ErrorResult("EX", "내부 오류");
    }

}
