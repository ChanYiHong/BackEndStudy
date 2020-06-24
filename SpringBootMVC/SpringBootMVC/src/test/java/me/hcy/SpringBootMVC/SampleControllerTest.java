package me.hcy.SpringBootMVC;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlHeading1;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
//import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringRunner.class)
@WebMvcTest(SampleController.class)
public class SampleControllerTest {

    // 가짜 Servlet container
    @Autowired
    MockMvc mockMvc;

    @Test
    public void hello() throws Exception {
        // 요청 "/"
        // 응답
        // - 모델 name : chanyi
        // - 뷰 이름 : hello

        // mockMVC로 get요청을 하면
        mockMvc.perform(get("/hellooo"))
                // 일단 status 체크하고
                .andExpect(status().isOk())
                .andDo(print())
                // 그다음에 view name이 hello
                .andExpect(view().name("hello"))
                .andExpect(model().attribute("name", is("chanyi")))
                .andExpect(content().string(containsString("chanyi")));

    }


    //WebClient를 주입받을 수 있음. (HTML Unit에 있음)
    @Autowired
    WebClient webClient;

    //WebClient를 통해서 getPage 요청을 보낼 수 있음
    //Html 특화된 test
    @Test
    public void helloo() throws Exception {
        HtmlPage page = webClient.getPage("/hellooo");
        HtmlHeading1 h1 = page.getFirstByXPath("//h1");
        assertThat(h1.getTextContent()).isEqualToIgnoringCase("chanyi");
    }
}


/**

 Thymeleaf가 독자적으로 최종적인 뷰를 완성함
 실질적으로 servlet container의 개입이 없음.

 JSP 를 view template engine으로 사용하면 렌더링 한 결과를 확인하진 못함.

 Thymeleaf는 Servlet container에 독립적인 엔진이기 때문에
 view에 랜더링 되는 결과까지 확인할 수 있음


 html Unit이라는 효율적인 테스팅 툴도 있음. 다음시간


 이제 View를 만들어 봅시다
 Thymeleaf를 학습해야 함

 모델이 전달받은 데이터를 이 view에 어떻게 출력을 할 것인가?

 가장 중요한건 html에 xml namespace를 추가해야 함




 **/