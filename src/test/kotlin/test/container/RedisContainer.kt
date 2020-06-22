package test.container

import org.testcontainers.containers.GenericContainer

class RedisContainer(version: String = "latest") : GenericContainer<RedisContainer>("redis:$version")