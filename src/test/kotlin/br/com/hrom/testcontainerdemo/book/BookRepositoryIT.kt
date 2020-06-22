package br.com.hrom.testcontainerdemo.book

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.test.context.ContextConfiguration
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import test.container.RedisContainer

@SpringBootTest
@ContextConfiguration(initializers = [BookRepositoryIT.Initializer::class])
@Testcontainers
class BookRepositoryIT {

    @Autowired
    lateinit var bookRepository: BookRepository

    @Test
    fun save() {
        // given
        val book = Book(name = "Ruroni Kenshin - 1", author = "Nobuhiro Watsuki")

        // when
        bookRepository.save(book)

        // then
        assertThat(redis.execInContainer("redis-cli", "keys", "*").stdout.lines()).contains("book:"+book.name)
    }

    class Initializer: ApplicationContextInitializer<ConfigurableApplicationContext> {
        override fun initialize(applicationContext: ConfigurableApplicationContext) {
            TestPropertyValues.of(
                    "spring.redis.host=${redis.containerIpAddress}",
                    "spring.redis.port=${redis.firstMappedPort}"
            ).applyTo(applicationContext.environment)
        }
    }

    companion object {
        @Container
        val redis: RedisContainer = RedisContainer().withExposedPorts(6379)
    }
}