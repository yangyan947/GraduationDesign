package com.tianm;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Created by SunYi on 2016/3/25/0025.
 */
@Configuration
@EnableAutoConfiguration
@ComponentScan
@PropertySource("classpath:core.properties")
public class TestRepositoryConfig {
}
