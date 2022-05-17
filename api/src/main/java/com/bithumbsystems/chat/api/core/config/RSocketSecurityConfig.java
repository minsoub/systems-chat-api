package com.bithumbsystems.chat.api.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.rsocket.RSocketStrategies;
import org.springframework.messaging.rsocket.annotation.support.RSocketMessageHandler;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.rsocket.EnableRSocketSecurity;
import org.springframework.security.config.annotation.rsocket.RSocketSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.rsocket.core.PayloadSocketAcceptorInterceptor;

@Configuration
@EnableRSocketSecurity
@EnableReactiveMethodSecurity
class RSocketSecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    RSocketMessageHandler messageHandler(final RSocketStrategies strategies) {
        final var handler = new RSocketMessageHandler();
//        handler.getArgumentResolverConfigurer().addCustomResolver(new AuthenticationPrincipalArgumentResolver());
        handler.setRSocketStrategies(strategies);
        return handler;
    }

    @Bean
    MapReactiveUserDetailsService authentication() {
        //This is NOT intended for production use (it is intended for getting started experience only)
        final UserDetails user = User.withUsername("user1")
                .password(passwordEncoder().encode("pass"))
                .roles("USER")
                .build();

        final UserDetails user2 = User.withUsername("user2")
                .password(passwordEncoder().encode("pass"))
                .roles("NONE")
                .build();

        return new MapReactiveUserDetailsService(user, user2);
    }

    @Bean
    PayloadSocketAcceptorInterceptor authorization(final RSocketSecurity security) {
        security.authorizePayload(authorize ->
                authorize
                        .setup().permitAll()
                        .anyExchange().authenticated()
        ).simpleAuthentication(Customizer.withDefaults());
        return security.build();
    }
}
