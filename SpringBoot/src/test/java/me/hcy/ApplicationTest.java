package me.hcy;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;



@RunWith(SpringRunner.class)
@TestPropertySource(locations = "classpath:/test.properties")
@SpringBootTest
public class ApplicationTest {

    @Autowired
    Environment environment;

    @Test
    public void contextLoads() {
        //assertThat(environment.getProperty("chanyi.name")).isEqualTo("hong");
    }

}
