package com.example.springbatchjenkinspractice;

import com.example.springbatchjenkinspractice.entity.Post;
import com.example.springbatchjenkinspractice.entity.PostRepository;
import java.util.List;
import java.util.stream.IntStream;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.transaction.annotation.Transactional;

@SpringBootApplication
public class SpringBatchJenkinsPracticeApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBatchJenkinsPracticeApplication.class, args);
    }

}
