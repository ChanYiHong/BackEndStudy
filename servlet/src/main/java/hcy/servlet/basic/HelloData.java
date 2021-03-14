package hcy.servlet.basic;

import lombok.Getter;
import lombok.Setter;

// JSON은 그대로 사용안하고 객체로 바꿔 쓰기 때문에 JSON 형식의 데이터를 Parsing 하기 위해서 만듬.
@Getter // 자바 빈 (프로퍼티) 접근법.
@Setter
public class HelloData {

    private String username;
    private int age;

}
