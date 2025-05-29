package com.pl.ing.crc.service.domain.kafka

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.convertValue
import com.pl.ing.crc.service.domain.model.elasticsearch.DomainObject
import com.pl.ing.crc.service.domain.model.elasticsearch.EventDTO
import com.pl.ing.crc.service.domain.model.kafka.MessageToMicroB
import com.pl.ing.crc.service.domain.repositories.elasticsearch.StateStoreRepository
import mu.KLogging
import org.springframework.messaging.Message
import reactor.core.publisher.Flux
import java.time.Instant
import java.util.function.Function

internal class MicroserviceBResponseProcessor(
    private val stateStoreRepository: StateStoreRepository, private val objectMapper: ObjectMapper
) {

    fun process(): Function<Flux<Message<Map<String, Any?>>>, Flux<DomainObject>> = Function { input ->
        input.map { message ->
            EventDTO(
                message.payload["messageId"] as String,
                message.payload["aggregateId"] as String,
                objectMapper.convertValue<MessageToMicroB>(message.payload),
                Instant.now().toEpochMilli()
            )
        }.flatMap { eventDto ->
                stateStoreRepository.findAll().filter { it.aggregateId == eventDto.aggregateId }
                    .sort { o1, o2 -> o1.timestamp.compareTo(o2.timestamp) }.collectList().map {
                        val objectToCorrect = it.last()
                        DomainObject(objectToCorrect.messageId, eventDto.eventBody.fieldA, eventDto.eventBody.fieldB)
                    }.flatMap {
                        stateStoreRepository.save(eventDto).thenReturn(it)
                    }
            }.doOnNext {
                logger.info { "Sending DomainObject to service-c: $it" }
            }
    }

    companion object : KLogging()
}
