package com.pl.ing.crc.service.domain.kafka

import com.pl.ing.crc.service.domain.model.kafka.MessageFromMicroA
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.convertValue
import com.pl.ing.crc.service.domain.model.elasticsearch.DomainObject
import com.pl.ing.crc.service.domain.repositories.elasticsearch.DomainObjectRepository
import mu.KLogging
import org.springframework.messaging.Message
import reactor.core.publisher.Flux
import java.util.function.Consumer
import java.util.UUID



class MicroserviceASaveRequestConsumer(
    private val domainObjectRepository: DomainObjectRepository,
    private val objectMapper: ObjectMapper
) {

    fun process(): Consumer<Flux<Message<Map<String, Any?>>>> = Consumer { input ->
        input
            .map { msg ->
                val messageFromMicroA = objectMapper.convertValue<MessageFromMicroA>(msg.payload)

                DomainObject(
                    id = UUID.randomUUID().toString(),
                    fieldA = messageFromMicroA.fieldA,
                    fieldB = messageFromMicroA.fieldB,
                )
            }
            .flatMap { domainObject ->
                domainObjectRepository.save(domainObject)
            }
            .doOnNext {
                logger.info { "Saved DomainObject with ID ${it.id}" }
            }
            .subscribe()
    }

    companion object : KLogging()
}
