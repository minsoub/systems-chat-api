package com.bithumbsystems.chat.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoReactiveAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.config.EnableReactiveMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import org.springframework.web.reactive.config.EnableWebFlux;



@SpringBootApplication(scanBasePackages = "com.bithumbsystems")
@EnableWebFlux
@ConfigurationPropertiesScan
@EnableReactiveMongoRepositories("com.bithumbsystems.persistence.mongodb")
@EnableReactiveMongoAuditing
public class ChatApplication {

  public static void main(String[] args) {
    SpringApplication.run(ChatApplication.class, args);
  }

  @Profile("dev|prod")
  @EnableAutoConfiguration(
      exclude = {
          MongoAutoConfiguration.class,
          MongoReactiveAutoConfiguration.class,
          MongoDataAutoConfiguration.class,
          EmbeddedMongoAutoConfiguration.class
      })
  static class WithoutAutoConfigurationMongo{}
}