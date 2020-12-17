package com.example.streamsecurityissue

import io.rsocket.core.RSocketServer
import io.rsocket.core.Resume
import org.springframework.boot.rsocket.server.RSocketServerCustomizer
import org.springframework.stereotype.Component

@Component
class RSocketResumptionConfig : RSocketServerCustomizer {

  override fun customize(rSocketServer: RSocketServer) {
    rSocketServer.resume(Resume());
  }
}