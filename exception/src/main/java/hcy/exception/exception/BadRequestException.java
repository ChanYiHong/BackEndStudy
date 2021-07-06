package hcy.exception.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// annotation 찾은 다음에 code, reason 가져와서 sendError를 알아서 해줌.
// sendError를 호출했기 때문에 이 경우에는 WAS에서 다시 오류 페이지 (/error) 를 내부 요청함..
@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "error.bad")
public class BadRequestException extends RuntimeException {



}
