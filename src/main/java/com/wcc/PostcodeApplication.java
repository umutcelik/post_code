package com.wcc;


import com.wcc.config.ApplicationConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication()
@Import({ApplicationConfig.class})
public class PostcodeApplication {
    public static void main(String[] args) {
        SpringApplication.run(PostcodeApplication.class, args);
    }

}
