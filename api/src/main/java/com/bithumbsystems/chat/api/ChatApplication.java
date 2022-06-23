package com.bithumbsystems.chat.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
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


//  @Profile("dev|prod|eks-dev")
//  @EnableAutoConfiguration(
//      exclude = {
//          MongoAutoConfiguration.class,
//          MongoReactiveAutoConfiguration.class,
//          MongoDataAutoConfiguration.class,
//          EmbeddedMongoAutoConfiguration.class
//      })
//  static class WithoutAutoConfigurationMongo{}
}