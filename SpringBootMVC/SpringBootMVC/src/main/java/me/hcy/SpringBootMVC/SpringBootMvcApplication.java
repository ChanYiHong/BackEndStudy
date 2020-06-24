package me.hcy.SpringBootMVC;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class SpringBootMvcApplication {

	// test를 요청하면 간단하게 메세지를 화면에 뿌려봅시다.
	// 아주 간단한 rest API
	// 실행하면 기본 포트는 8080. 이걸 REST API 서버고, 이걸 요청하는
	@CrossOrigin(origins = "http://localhost:18080")
	@GetMapping("/test")
	public String test(){
		return "Hello";
	}

	public static void main(String[] args) {

		SpringApplication.run(SpringBootMvcApplication.class, args);
	}
}


