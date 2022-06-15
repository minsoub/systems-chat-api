package com.bithumbsystems.chat.api.core.config.resolver;

import com.bithumbsystems.persistence.mongodb.message.model.Account;
import com.bithumbsystems.persistence.mongodb.message.model.enums.Role;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.messaging.Message;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.messaging.handler.invocation.reactive.AuthenticationPrincipalArgumentResolver;
import org.springframework.security.oauth2.jwt.Jwt;
import reactor.core.publisher.Mono;

@Slf4j
public class CustomArgumentResolver extends AuthenticationPrincipalArgumentResolver {

  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    Class<?> parameterType = parameter.getParameterType();
    return Account.class.equals(parameterType);
  }

  @Override
  public Mono<Object> resolveArgument(MethodParameter parameter, Message<?> message) {
    // @formatter:off
    return ReactiveSecurityContextHolder.getContext()
        .map(SecurityContext::getAuthentication)
        .flatMap((a) -> {
          Jwt principal = (Jwt) a.getPrincipal();
          var accountId = principal.getClaims().get("account_id").toString();
          var email = principal.getClaims().get("iss").toString();
          var role = principal.getClaims().get("ROLE").toString();
        return Mono.just(new Account(accountId, email, role.equals(Role.USER.name()) ? Role.USER : Role.ADMIN));
        });
  }
}
