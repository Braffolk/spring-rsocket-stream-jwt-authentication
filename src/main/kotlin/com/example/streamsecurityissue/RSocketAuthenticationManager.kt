package com.example.streamsecurityissue


import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import java.util.*
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority



@Component
class RSocketAuthenticationManager(): ReactiveAuthenticationManager {
  @Override
  override fun authenticate(authentication: Authentication): Mono<Authentication> {
    println("authenticate called with key ${authentication.credentials.toString()}")
    val authorities: MutableList<GrantedAuthority> = ArrayList()
    return Mono.just(UsernamePasswordAuthenticationToken("test", null, authorities))
  }
}