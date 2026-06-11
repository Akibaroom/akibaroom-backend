package com.akibaroom

import org.springframework.boot.fromApplication
import org.springframework.boot.with


fun main(args: Array<String>) {
	fromApplication<AkibaroomApplication>().with(TestcontainersConfiguration::class).run(*args)
}
