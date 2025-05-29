package com.pl.ing.crc.service.domain.kafka

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.convertValue
import com.pl.ing.crc.service.domain.model.elasticsearch.DomainObject
import com.pl.ing.crc.service.domain.model.elasticsearch.EventDTO
import com.pl.ing.crc.service.domain.model.kafka.MessageFromMicroA
import com.pl.ing.crc.service.domain.repositories.elasticsearch.DomainObjectRepository
import mu.KLogging
import org.springframework.messaging.Message
import reactor.core.publisher.Flux
import java.time.Instant
import java.util.function.Consumer

internal class MicroserviceAResponseConsumer(
    private val domainObjectRepository: DomainObjectRepository,
    private val objectMapper: ObjectMapper
) {
    fun process(): Consumer<Flux<Message<Map<String, Any?>>>> = Consumer { input ->
        input
            .map { message ->
                EventDTO(
                    message.payload["messageId"] as String,
                    message.payload["aggregateId"] as String,
                    objectMapper.convertValue<MessageFromMicroA>(message.payload),
                    Instant.now().toEpochMilli()
                )
            }
            .flatMap { eventDto ->
                domainObjectRepository.save(
                    DomainObject(eventDto.messageId, eventDto.eventBody.fieldA,eventDto.eventBody.fieldB)
                )
            }
            .doOnNext {
                logger.info { "New domain objects saved." }
            }
            .subscribe()
    }

    companion object : KLogging()
}