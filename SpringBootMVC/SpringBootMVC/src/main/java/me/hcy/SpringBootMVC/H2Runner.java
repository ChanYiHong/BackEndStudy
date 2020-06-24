package me.hcy.SpringBootMVC;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Statement;

@Component
public class H2Runner implements ApplicationRunner {

    @Autowired
    DataSource dataSource;

    // 접속할 DB에 대한 정보 확인 가능
    // DataSourceProperties에 들어있음
    @Override
    public void run(ApplicationArguments args) throws Exception {
        try(Connection connection = dataSource.getConnection()) {
            connection.getMetaData().getURL();
            connection.getMetaData().getUserName();

            Statement statement = connection.createStatement();
            String sql = "CREATE TABLE USER(ID INTEGER NOT NULL, name VARCHAR(255), PRIMARY KEY(id)) INSERT INTO USER VALUES (1, 'chanyi')";
            statement.executeUpdate(sql);
        }
    }
}
