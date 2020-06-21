package me.hcy.SpringBootMVC.user;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    public void hello() throws Exception {
        mockMvc.perform(get("/hello"))
        .andExpect(status().isOk())
        .andExpect(content().string("hello"));
    }

    // JSON -> JSON
    @Test
    public void CreateUser_JSON() throws Exception {
        String userJson = "{\"username\":\"chanyi\", \"password\":\"123\"}";
        // Post 요청을 사용. /users/create라는 URL사용
        mockMvc.perform(post("/users/create")
                //content type은 MediaType이라는 spring이 제공하는 상수를 모아놓은 클래스에서 사용.
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                //응답을 요청하는데 Json 타입을 요청한다.
                .accept(MediaType.APPLICATION_JSON_UTF8)
                //응답 본문이 필요. userJson을 만들어서 넣음
                .content(userJson))
                //상태 체크
                    .andExpect(status().isOk())
                //일단 응답 결과에 Json이 나올거고, username으로 내가 넣어줬던 chanyi라고 나오길 바라
                    .andExpect(jsonPath("$.username",
                            is(equalTo("chanyi"))))
                //password return
                    .andExpect(jsonPath("$.password",
                            is(equalTo("123"))));
    }

    // JSON -> XML
    // XML은 의존성 추가 필요
    @Test
    public void CreateUser_XML() throws Exception {
        String userJson = "{\"username\":\"chanyi\", \"password\":\"123\"}";
        // Post 요청을 사용. /users/create라는 URL사용
        mockMvc.perform(post("/users/create")
                //content type은 MediaType이라는 spring이 제공하는 상수를 모아놓은 클래스에서 사용.
                //요청은 JSON
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                //응답은 XML로
                .accept(MediaType.APPLICATION_XML)
                //응답 본문이 필요. userJson을 만들어서 넣음
                .content(userJson))
                //상태 체크
                .andExpect(status().isOk())
                //Xpath로 확인.
                .andExpect(xpath("/User/username")
                        .string("chanyi"))
                //password return
                .andExpect(xpath("/User/password")
                        .string("123"));
    }
}
