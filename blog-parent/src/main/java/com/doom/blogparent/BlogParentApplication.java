package com.doom.blogparent;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class BlogParentApplication {

    public static void main(String[] args) {
        SpringApplication.run(BlogParentApplication.class, args);
    }

}
