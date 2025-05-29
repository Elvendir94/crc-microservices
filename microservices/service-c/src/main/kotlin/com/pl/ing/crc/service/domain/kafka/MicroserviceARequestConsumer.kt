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
                EventDTO(
                    message.payload["messageId"] as String,
                    message.payload["aggregateId"] as String,
                    objectMapper.convertValue<MessageFromMicroA>(message.payload),
                    Instant.now().toEpochMilli()
                )
            }.doOnNext {
                logger.info {
                    "EventDTO: messageId: ${it.messageId}, aggregateId: ${it.aggregateId}, fieldA: ${it.eventBody.fieldA}, ${it.eventBody.fieldB}"
                }
            }
            .filter { true }
            .flatMap { eventDto ->
                stateStoreRepository.findAll()
                    .filter { it.aggregateId == eventDto.aggregateId }
                    .sort { o1, o2 -> o1.timestamp.compareTo(o2.timestamp) }
                    .collectList()
                    .map {
                        // Merge events from list into single event (based on business logic)
                        val objectToCorrect = it.last()

                        DomainObject(objectToCorrect.messageId, eventDto.eventBody.fieldA, eventDto.eventBody.fieldB)
                    }.flatMap { domainObject ->
                        domainObjectRepository.save(domainObject)
                    }.flatMap {
                        stateStoreRepository.save(eventDto)
                    }
            }
            .doOnNext {
                logger.info { "Values fieldA and fieldB received from service A. Values ${it.eventBody.fieldA} | ${it.eventBody.fieldB}" }
            }.subscribe()
    }

    companion object : KLogging()
}
