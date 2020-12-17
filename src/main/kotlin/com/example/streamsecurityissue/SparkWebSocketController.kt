package com.example.streamsecurityissue

import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.rsocket.RSocketRequester
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Controller
class SparkWebSocketController() {
  @MessageMapping("stream")
  fun subscribeToResponseStream(str: String, rSocketRequester: RSocketRequester, @AuthenticationPrincipal jwt: String): Flux<String> {
    println("stream message mapped")
    return Flux.just("hello world!")
  }

  @MessageMapping("response")
  fun subscribeToResponse(str: String, rSocketRequester: RSocketRequester, @AuthenticationPrincipal jwt: String): Mono<String> {
    println("response message mapped")
    return Mono.just("hello world!")
  }
}