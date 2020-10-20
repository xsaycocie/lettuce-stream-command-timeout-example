package com.example.lettucesample;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Configuration
public class RedisConfiguration {
    private String url = "redis://localhost";
    private String host = "localhost";
    private Integer port = 6379;
    private Integer timeout = 1;
    private Integer maxStreamLength = 10000;
    private Boolean useSsl = false;
    private Integer poolMaxTotal = 200;
    private Integer poolMaxIdle = 100;
    private Integer poolMinIdle = 50;
    private Boolean poolBlockWhenExhausted = true;
    private Integer poolMaxWaitMillis = 1;
    private Integer streamExpireSeconds = 1800;
}