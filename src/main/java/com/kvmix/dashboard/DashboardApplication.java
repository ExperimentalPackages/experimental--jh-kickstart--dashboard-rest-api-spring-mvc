package com.kvmix.dashboard;

import jakarta.annotation.PostConstruct;
import java.util.TimeZone;

import com.kvmix.dashboard.config.ApplicationProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({ApplicationProperties.class})
public class DashboardApplication {

  public static void main(String[] args) {
    SpringApplication.run(DashboardApplication.class, args);
  }

  @PostConstruct
  public void init() {
    TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
  }

}
