package com.example.springbatchjenkinspractice.config;


import com.example.springbatchjenkinspractice.entity.Post;
import com.example.springbatchjenkinspractice.entity.PostRepository;
import jakarta.persistence.EntityManagerFactory;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepListener;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class SampleBatchConfiguration {
    private static final String JOB_NAME = "SAMPLE_JOB";
    private static final String STEP_NAME = "SAMPLE_STEP";
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final EntityManagerFactory entityManagerFactory;

    private final PostRepository postRepository;
    @Bean(JOB_NAME)
    public Job webCrawleJob() {
        return new JobBuilder(JOB_NAME, jobRepository)
                .start(step(null))
                .build();
    }

    @Bean(STEP_NAME)
    @JobScope
    public Step step(
            @Value("#{jobParameters[requestDate]}") String requestDate
    ) {
        return new StepBuilder(STEP_NAME, jobRepository)
                .chunk(10, transactionManager)
                .reader(new JpaPagingItemReaderBuilder<>()
                        .name("jpa_step")
                        .pageSize(10)
                        .queryString("select p from Post p")
                        .entityManagerFactory(entityManagerFactory).build())
                .writer(chunk -> log.info("{}", chunk.getItems()))
                .build();
    }


    @Bean
    JobExecutionListener jobExecutionListener(){
        return new JobExecutionListener() {

            @Transactional
            @Override
            public void beforeJob(JobExecution jobExecution) {
                postRepository.saveAll(IntStream.iterate(1, i -> i + 1)
                        .limit(100)
                        .mapToObj(String::valueOf)
                        .map(Post::new)
                        .toList());
            }
        };
    }

}
