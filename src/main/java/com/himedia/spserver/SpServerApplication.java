package com.himedia.spserver;

import org.springframework.ai.vectorstore.pgvector.autoconfigure.PgVectorStoreAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(
        exclude = {
                PgVectorStoreAutoConfiguration.class, // 자동 vectorStore 빈 생성 막기
        }
)

public class SpServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpServerApplication.class, args);
    }

}
