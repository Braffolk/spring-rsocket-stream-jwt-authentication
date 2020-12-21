package com.example.streamsecurityissue

import org.springframework.messaging.rsocket.annotation.support.RSocketMessageHandler
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity
import org.springframework.security.config.annotation.rsocket.EnableRSocketSecurity
import org.springframework.security.config.annotation.rsocket.RSocketSecurity
import org.springframework.security.config.annotation.rsocket.RSocketSecurity.AuthorizePayloadsSpec
import org.springframework.security.rsocket.core.PayloadSocketAcceptorInterceptor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.messaging.rsocket.RSocketStrategies
import org.springframework.security.messaging.handler.invocation.reactive.AuthenticationPrincipalArgumentResolver
import org.springframework.web.util.pattern.PathPatternRouteMatcher


@Configuration
@EnableRSocketSecurity
@EnableReactiveMethodSecurity
class RSocketConfig {
  @Autowired
  lateinit var rSocketAuthenticationManager: RSocketAuthenticationManager

  @Bean
  fun rSocketMessageHandler(strategies: RSocketStrategies?): RSocketMessageHandler? {
    println("messageHandler initiated!")
    val handler = RSocketMessageHandler()
    handler.argumentResolverConfigurer.addCustomResolver(AuthenticationPrincipalArgumentResolver())
    handler.routeMatcher = PathPatternRouteMatcher()
    handler.rSocketStrategies = strategies!!
    return handler
  }

  @Bean
  fun authorization(rsocket: RSocketSecurity): PayloadSocketAcceptorInterceptor {
    rsocket.authorizePayload { authorize: AuthorizePayloadsSpec ->
      authorize
          .anyRequest().authenticated()
          .anyExchange().permitAll()
    }
        .jwt { jwtSpec: RSocketSecurity.JwtSpec ->
            jwtSpec.authenticationManager(rSocketAuthenticationManager)
        }

    return rsocket.build()
  }

  @Bean
  fun rSocketStrategies() = RSocketStrategies.builder()
      .routeMatcher(PathPatternRouteMatcher())
      .build()
}