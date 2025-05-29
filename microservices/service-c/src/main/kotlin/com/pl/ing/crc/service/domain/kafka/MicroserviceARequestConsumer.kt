package com.pl.ing.crc.service.domain.kafka

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.convertValue
import com.pl.ing.crc.service.domain.model.elasticsearch.DomainObject
import com.pl.ing.crc.service.domain.model.elasticsearch.EventDTO
import com.pl.ing.crc.service.domain.model.kafka.MessageFromMicroA
import com.pl.ing.crc.service.domain.repositories.elasticsearch.DomainObjectRepository
import com.pl.ing.crc.service.domain.repositories.elasticsearch.StateStoreRepository
import mu.KLogging
import org.springframework.messaging.Message
import reactor.core.publisher.Flux
import java.time.Instant
import java.util.function.Consumer

internal class MicroserviceARequestConsumer(
    private val stateStoreRepository: StateStoreRepository,
    private val domainObjectRepository: DomainObjectRepository,
    private val objectMapper: ObjectMapper
) {
    fun process(): Consumer<Flux<Message<Map<String, Any?>>>> = Consumer { input ->
        input
            .doOnNext {
                logger.info { "Received message from kafka from service-a" }
            }
            .map { message ->
                val eventDto = EventDTO(
                    message.payload["messageId"] as String,
                    message.payload["aggregateId"] as String,
                    objectMapper.convertValue<MessageFromMicroA>(message.payload),
                    Instant.now().toEpochMilli()
                )

                stateStoreRepository.save(eventDto)
            }
            .filter { true }
            .flatMap { inp ->
                inp.flatMap {
                    domainObjectRepository.save(DomainObject(it.eventBody.messageId, it.eventBody.fieldA, it.eventBody.fieldB))
                }
            }
            .doOnNext {
                logger.info {"Saved object in domainObjectRepository: ${it.fieldA} ${it.fieldB}"}
            }
            .subscribe()
    }

    companion object : KLogging()
}

