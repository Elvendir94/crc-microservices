package com.pl.ing.crc.service.domain.kafka

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.convertValue
import com.pl.ing.crc.service.domain.model.elasticsearch.DomainObject
import com.pl.ing.crc.service.domain.model.elasticsearch.EventDTO
import com.pl.ing.crc.service.domain.model.kafka.MessageToMicroB
import com.pl.ing.crc.service.domain.repositories.elasticsearch.StateStoreRepository
import mu.KLogging
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.messaging.Message
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import java.time.Instant
import java.util.function.Consumer

@Component
internal class MicroserviceBResponseConsumer(
        private val stateStoreRepository: StateStoreRepository,
        private val kafkaTemplate: KafkaTemplate<String, DomainObject>,
        private val objectMapper: ObjectMapper
) {

    fun process(): Consumer<Flux<Message<Map<String, Any?>>>> = Consumer { input ->
        input
                .map { message ->
                    EventDTO(
                            message.payload["messageId"] as String,
                            message.payload["aggregateId"] as String,
                            objectMapper.convertValue<MessageToMicroB>(message.payload),
                            Instant.now().toEpochMilli()
                    )
                }
                .doOnNext {
                    logger.info { "Received event from MicroB." }
                }
                .flatMap { eventDto ->
                    stateStoreRepository.findAll()
                            .filter { it.aggregateId == eventDto.aggregateId }
                            .sort { o1, o2 -> o1.timestamp.compareTo(o2.timestamp) }
                            .collectList()
                            .map {
                                val objectToCorrect = it.last()
                                DomainObject(
                                        objectToCorrect.messageId,
                                        eventDto.eventBody.fieldA,
                                        eventDto.eventBody.fieldB
                                )
                            }
                            .doOnNext { domainObject ->
                                kafkaTemplate.send("processed-domain-object-topic", domainObject.id, domainObject)
                                logger.info { "Sent DomainObject to Kafka: $domainObject" }
                            }
                            .then(stateStoreRepository.save(eventDto))
                }
                .subscribe()
    }

    companion object : KLogging()
}
