package pwr.smart.home.control.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
@EnableScheduling
public class AsyncConfig {
    @Bean(name = "threadPoolTaskExecutor")
    @Primary
    public Executor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setMaxPoolSize(20);

        return executor;
    }

    @Bean(name = "TemperatureThreadPoolTaskExecutor")
    public Executor TemperatureThreadPoolTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setQueueCapacity(5);
        executor.setMaxPoolSize(15);

        return executor;
    }

    @Bean(name = "HumidityThreadPoolTaskExecutor")
    public Executor HumidityThreadPoolTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setQueueCapacity(5);
        executor.setMaxPoolSize(15);

        return executor;
    }

    @Bean(name = "FilterThreadPoolTaskExecutor")
    public Executor FilterThreadPoolTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setQueueCapacity(5);
        executor.setMaxPoolSize(15);

        return executor;
    }
}
