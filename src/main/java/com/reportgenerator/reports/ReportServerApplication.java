package com.reportgenerator.reports;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

@SpringBootApplication
public class ReportServerApplication extends SpringBootServletInitializer {

  public static void main(String[] args) {
    System.out.println("Application Starting...... check the logs in logs/report.log");
    SpringApplication.run(ReportServerApplication.class, args);
  }

  @Override
  protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
    return builder.sources(ReportServerApplication.class);
  }
}
