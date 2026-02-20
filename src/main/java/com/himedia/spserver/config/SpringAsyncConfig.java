package com.himedia.spserver.config;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.lang.reflect.Method;
import java.util.concurrent.Executor;

// 비동기 실행을 위한 커스텀 executor 생성
@Configuration
@EnableAsync
public class SpringAsyncConfig implements AsyncConfigurer {
    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(100);
        executor.setQueueCapacity(50);
        executor.setThreadNamePrefix("custom-");
        executor.initialize();
        return executor;
    }

    // 비동기 실행의 예외 처리를 위한 커스텀 예외 핸들러 오버라이드
    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new CustomAsyncExceptionHandler();
    }

    // 커스텀 예외 핸들러 생성
    public class CustomAsyncExceptionHandler
            implements AsyncUncaughtExceptionHandler {
        @Override
        public void handleUncaughtException(Throwable throwable, Method method, Object... params) {

            System.out.println("예외 발생 : " + throwable.getMessage());
            System.out.println("메서드 : " + method.getName());
            for (Object param : params) {
                System.out.println("파라미터 : " + param);
            }
        }

    }
}

