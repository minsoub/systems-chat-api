package com.bithumbsystems.chat.api.core.config;

import com.bithumbsystems.chat.api.core.config.resolver.CustomArgumentResolver;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.messaging.handler.invocation.reactive.ArgumentResolverConfigurer;
import org.springframework.messaging.rsocket.RSocketStrategies;
import org.springframework.messaging.rsocket.annotation.support.RSocketMessageHandler;
import org.springframework.security.messaging.handler.invocation.reactive.CurrentSecurityContextArgumentResolver;
import org.springframework.security.rsocket.metadata.SimpleAuthenticationEncoder;
import org.springframework.web.util.pattern.PathPatternRouteMatcher;

@Configuration
class RSocketConfig {

    public static final String PATTERN = "yyyy-MM-dd'T'HH:mm:ss";

    @Bean
    RSocketStrategies rSocketStrategies() {
        return RSocketStrategies.builder()
            .decoders(decoders -> decoders.add(new Jackson2JsonDecoder(objectMapper())))
            .encoders(encoders -> encoders.add(new Jackson2JsonEncoder(objectMapper())))
            .encoders(encoders -> encoders.add(new SimpleAuthenticationEncoder()))
            .build();
    }

    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        JavaTimeModule module = new JavaTimeModule();
        LocalDateTimeSerializer localDateTimeSerializer = new LocalDateTimeSerializer(
            DateTimeFormatter.ofPattern(PATTERN));
        LocalDateTimeDeserializer localDateTimeDeserializer = new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(PATTERN));
        module.addSerializer(LocalDateTime.class, localDateTimeSerializer);
        module.addDeserializer(LocalDateTime.class, localDateTimeDeserializer);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(module);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE); // snake case 로 변환
        return objectMapper;
    }

    @Bean
    RSocketMessageHandler messageHandler(RSocketStrategies rSocketStrategies, BeanFactory beanFactory) {
        BeanFactoryResolver resolver = new BeanFactoryResolver(beanFactory);
        CustomArgumentResolver principal = new CustomArgumentResolver();
        principal.setBeanResolver(resolver);
        CurrentSecurityContextArgumentResolver context = new CurrentSecurityContextArgumentResolver();
        context.setBeanResolver(resolver);
        RSocketMessageHandler messageHandler = new RSocketMessageHandler();
        messageHandler.setRSocketStrategies(rSocketStrategies);
        messageHandler.setRouteMatcher(new PathPatternRouteMatcher());
        ArgumentResolverConfigurer args = messageHandler
            .getArgumentResolverConfigurer();
        args.addCustomResolver(principal);
        args.addCustomResolver(context);
        return messageHandler;
    }
}
