package com.bithumbsystems.chat.api.core.config;

import com.bithumbsystems.chat.api.core.config.property.AwsProperties;
import javax.crypto.spec.SecretKeySpec;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.config.annotation.rsocket.EnableRSocketSecurity;
import org.springframework.security.config.annotation.rsocket.RSocketSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtReactiveAuthenticationManager;
import org.springframework.security.rsocket.core.PayloadSocketAcceptorInterceptor;

@Configuration
@EnableRSocketSecurity
@RequiredArgsConstructor
class RSocketSecurityConfig {

    private final AwsProperties awsProperties;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public ReactiveJwtDecoder reactiveJwtDecoder() {
        SecretKeySpec secretKey = new SecretKeySpec(awsProperties.getJwtSecretKey().getBytes(), MacAlgorithm.HS512.getName());

        return NimbusReactiveJwtDecoder.withSecretKey(secretKey)
            .macAlgorithm(MacAlgorithm.HS512)
            .build();
    }

    @Bean
    public PayloadSocketAcceptorInterceptor rsocketInterceptor(RSocketSecurity rsocket) {
        rsocket.authorizePayload(authorize ->
                authorize
                    .setup().permitAll()
                    .anyExchange().authenticated()
            )
            .jwt(jwtSpec -> {
                try {
                    jwtSpec.authenticationManager(jwtReactiveAuthenticationManager(reactiveJwtDecoder()));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });

        return rsocket.build();
    }


    public ReactiveAuthenticationManager jwtReactiveAuthenticationManager(ReactiveJwtDecoder reactiveJwtDecoder) {
        return new JwtReactiveAuthenticationManager(reactiveJwtDecoder);
    }
}
