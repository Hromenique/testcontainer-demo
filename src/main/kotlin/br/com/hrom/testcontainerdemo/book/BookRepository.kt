package br.com.hrom.testcontainerdemo.book

import br.com.hrom.testcontainerdemo.infra.repository.BaseJsonRedisTemplateRepository
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.stereotype.Repository

internal const val BOOKS_SCHEMA_NAME: String = "book:"


@Repository
class BookRepository(connectionFactory: RedisConnectionFactory, objectMapper: ObjectMapper) :
        BaseJsonRedisTemplateRepository<Book>(connectionFactory, objectMapper) {

    fun save(book: Book) = jsonRedisTemplate.opsForValue().set(BOOKS_SCHEMA_NAME + book.name, book)

    fun get(bookName: String): Book? = jsonRedisTemplate.opsForValue().get(BOOKS_SCHEMA_NAME + bookName)

    fun list(): List<Book> = jsonRedisTemplate
            .keys("$BOOKS_SCHEMA_NAME*")
            .mapNotNull { runCatching { get(it) }.getOrDefault(null) }
            .toList()
}