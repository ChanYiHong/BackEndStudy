package hcy.exception.exhandler;

import lombok.AllArgsConstructor;
import lombok.Data;

/** 오류가 나면 클라이언트에게 보내줄 단순한 JSON **/
@Data
@AllArgsConstructor
public class ErrorResult {

    private String code;
    private String message;

}
