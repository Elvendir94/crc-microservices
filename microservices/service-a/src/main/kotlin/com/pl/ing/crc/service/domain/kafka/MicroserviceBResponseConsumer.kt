package com.pl.ing.crc.service.domain.kafka

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.convertValue
import com.pl.ing.crc.service.domain.model.elasticsearch.DomainObject
import com.pl.ing.crc.service.domain.model.elasticsearch.EventDTO
import com.pl.ing.crc.service.domain.model.kafka.MessageFromMicroB
import com.pl.ing.crc.service.domain.model.kafka.MessageToMicroB
import com.pl.ing.crc.service.domain.model.kafka.MessageToServiceC
import com.pl.ing.crc.service.domain.repositories.elasticsearch.StateStoreRepository
import mu.KLogging
import org.springframework.messaging.Message
import org.springframework.messaging.support.MessageBuilder
import reactor.core.publisher.Flux
import java.time.Instant
import java.util.function.Function

internal class MicroserviceBResponseConsumer(
    private val stateStoreRepository: StateStoreRepository,
    private val objectMapper: ObjectMapper
) {

    fun process(): Function<Flux<Message<Map<String, Any?>>>, Flux<Message<MessageToServiceC>>> = Function { input ->
        input
            .map { message ->
                EventDTO(
                    message.payload["messageId"] as String,
                    message.payload["aggregateId"] as String,
                    objectMapper.convertValue<MessageToMicroB>(message.payload),
                    Instant.now().toEpochMilli()
                )
            }
            .filter { true } // Add any filtering logic if needed
            .doOnNext {
                logger.info { "Put some processing here." }
            }
            .flatMap { eventDto ->
                stateStoreRepository.findAll()
                    .filter { it.aggregateId == eventDto.aggregateId }
                    .sort { o1, o2 -> o1.timestamp.compareTo(o2.timestamp) }
                    .collectList()
                    .flatMap { events ->
                        // Merge events from list into single event (based on business logic)
                        val objectToCorrect = events.last()
                        val domainObject = DomainObject(objectToCorrect.messageId, eventDto.eventBody.fieldA, eventDto.eventBody.fieldB)

                        // Save the event dto to state store
                        stateStoreRepository.save(eventDto)
                            .map { domainObject }
                    }
            }
            .map { domainObject ->
                // Create a message for service-c with DomainObject data
                val messageToServiceC = MessageToServiceC(
                    domainObject.id,
                    domainObject.fieldA,
                    domainObject.fieldB
                )
                MessageBuilder.withPayload(messageToServiceC).build()
            }
            .doOnNext {
                logger.info { "Sending domain object to service-c with id: ${it.payload.id}, fieldA: ${it.payload.fieldA}, fieldB: ${it.payload.fieldB}" }
            }
    }

    companion object : KLogging()
}
