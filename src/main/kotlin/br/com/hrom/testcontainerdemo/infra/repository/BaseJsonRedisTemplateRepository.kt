package br.com.hrom.testcontainerdemo.infra.repository

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.StringRedisSerializer
import java.lang.reflect.ParameterizedType

abstract class BaseJsonRedisTemplateRepository<T>(connectionFactory: RedisConnectionFactory, objectMapper: ObjectMapper) {

    private fun getTypeParameterClass(): Class<T> {
        val parameterizedType = javaClass.genericSuperclass as ParameterizedType
        return parameterizedType.actualTypeArguments[0] as Class<T>
    }

    val jsonRedisTemplate: RedisTemplate<String, T> = RedisTemplate<String, T>().also {
        it.connectionFactory = connectionFactory
        it.keySerializer = StringRedisSerializer()
        it.valueSerializer = Jackson2JsonRedisSerializer(getTypeParameterClass()).also { it.setObjectMapper(objectMapper) }
        it.afterPropertiesSet()
    }
}