package br.com.hrom.testcontainerdemo.book

import br.com.hrom.testcontainerdemo.config.ObjectMapperConfig
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.data.redis.connection.RedisStandaloneConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import test.container.RedisContainer

@Testcontainers
class BookRepositoryTest {

    @Container
    var redis: RedisContainer = RedisContainer().withExposedPorts(6379)

    private lateinit var bookRepository: BookRepository

    @BeforeEach
    fun setup() {
        val host = redis.containerIpAddress
        val port = redis.firstMappedPort
        val connectionFactory = LettuceConnectionFactory(RedisStandaloneConfiguration(host, port)).also { it.afterPropertiesSet() }
        val objectMapper = ObjectMapperConfig().objectMapper()
        bookRepository = BookRepository(connectionFactory = connectionFactory, objectMapper = objectMapper)
    }

    @Test
    fun save() {
        // given
        val book = Book(name = "Ruroni Kenshin - 1", author = "Nobuhiro Watsuki")

        // when
        bookRepository.save(book)

        // then
        assertThat(redis.execInContainer("redis-cli", "keys", "*").stdout.lines()[0]).isEqualTo("book:"+book.name)
    }
}