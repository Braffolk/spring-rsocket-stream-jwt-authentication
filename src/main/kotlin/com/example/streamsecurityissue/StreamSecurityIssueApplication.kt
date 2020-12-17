package com.example.streamsecurityissue

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.boot.runApplication

@SpringBootApplication
class StreamSecurityIssueApplication

fun main(args: Array<String>) {
	runApplication<StreamSecurityIssueApplication>(*args)
}

fun configure(application: SpringApplicationBuilder): SpringApplicationBuilder? {
	return application.sources(applicationClass)
}

private val applicationClass = StreamSecurityIssueApplication::class.java