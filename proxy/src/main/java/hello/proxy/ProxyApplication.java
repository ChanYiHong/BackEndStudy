package hello.proxy;

import hello.proxy.config.AppV1Config;
import hello.proxy.config.AppV2Config;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@Import({AppV1Config.class, AppV2Config.class}) // 컴포넌트 스캔 대상으로 만들기 위해 Import
@SpringBootApplication(scanBasePackages = "hello.proxy.app") // hello.proxy.app 하위만 컴포넌트 스캔.
public class ProxyApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProxyApplication.class, args);
	}

}
