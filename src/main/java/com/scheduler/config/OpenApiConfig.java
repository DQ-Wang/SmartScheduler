package com.scheduler.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI smartSchedulerOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Smart Study Scheduler API")
                        .description("面向 Dify / AI Agent 的智能学习任务调度 MCP 后端")
                        .version("1.0.0")
                        .contact(new Contact().name("SmartScheduler Team")));
    }
}
