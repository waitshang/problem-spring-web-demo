package com.shang.problem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;

@SpringBootApplication(exclude = ErrorMvcAutoConfiguration.class)
public class ProblemSpringWebResourceServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProblemSpringWebResourceServerApplication.class, args);
    }

}
