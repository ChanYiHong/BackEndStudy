package hcy.typeconverter.type;

import lombok.EqualsAndHashCode;
import lombok.Getter;

//"127.0.0.1:8080" 문자열이 오면 이 객체로 만드는 컨버터를 만들어 볼 것임.
@Getter
@EqualsAndHashCode
public class IpPort {

    private String ip;
    private int port;

    public IpPort(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }
}
