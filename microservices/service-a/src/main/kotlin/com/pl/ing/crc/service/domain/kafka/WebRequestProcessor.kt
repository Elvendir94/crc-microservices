package com.pl.ing.crc.service.domain.kafka

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.convertValue
import com.pl.ing.crc.service.domain.model.elasticsearch.EventDTO
import com.pl.ing.crc.service.domain.model.kafka.MessageToMicroB
import com.pl.ing.crc.service.domain.repositories.elasticsearch.StateStoreRepository
import mu.KLogging
import org.springframework.messaging.Message
import reactor.core.publisher.Flux
import java.time.Instant
import java.util.function.Function

internal class WebRequestProcessor(
    private val stateStoreRepository: StateStoreRepository,
    private val objectMapper: ObjectMapper
) {

    fun process(): Function<Flux<Message<Map<String, Any?>>>, Flux<MessageToMicroB>> = Function { input ->
        input.map { message ->
            EventDTO(
                message.payload["messageId"] as String,
                message.payload["aggregateId"] as String,
                objectMapper.convertValue<MessageToMicroB>(message.payload),
                Instant.now().toEpochMilli()
            )
        }
        .filter { true }
        .doOnNext {
            logger.info { "Put some processing here." }
        }
        .flatMap {
            stateStoreRepository.save(it)
        }.map {
            it.eventBody
        }.doOnNext {
            logger.info { "Sending message to micro B" }
        }
    }

    companion object : KLogging()
}