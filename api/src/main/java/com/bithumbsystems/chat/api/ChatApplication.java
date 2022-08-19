package com.bithumbsystems.chat.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoReactiveAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.data.mongodb.config.EnableReactiveMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import org.springframework.web.reactive.config.EnableWebFlux;



@SpringBootApplication(scanBasePackages = "com.bithumbsystems", exclude = {
    MongoAutoConfiguration.class,
    MongoReactiveAutoConfiguration.class,
    MongoDataAutoConfiguration.class,
    EmbeddedMongoAutoConfiguration.class
})
@EnableWebFlux
@ConfigurationPropertiesScan
@EnableReactiveMongoRepositories("com.bithumbsystems.persistence.mongodb")
@EnableReactiveMongoAuditing
public class ChatApplication {

  public static void main(String[] args) {
    SpringApplication app = new SpringApplication(ChatApplication.class);
    app.addListeners(new ApplicationPidFileWriter());
    app.run(args);
  }
}