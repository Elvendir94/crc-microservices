package com.pl.ing.crc.service.domain.kafka

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.convertValue
import com.pl.ing.crc.service.domain.model.elasticsearch.DomainObject
import com.pl.ing.crc.service.domain.model.kafka.MessageDomainObject
import com.pl.ing.crc.service.domain.repositories.elasticsearch.DomainObjectRepository
import mu.KLogging
import org.springframework.messaging.Message
import reactor.core.publisher.Flux
import java.util.function.Consumer

internal class ServiceADomainObjectConsumer(
    private val domainObjectRepository: DomainObjectRepository,
    private val objectMapper: ObjectMapper
) {

    fun process(): Consumer<Flux<Message<Map<String, Any?>>>> = Consumer { input ->
        input
            .map { message ->
                objectMapper.convertValue<MessageDomainObject>(message.payload)
            }
            .doOnNext {
                logger.info { "Received domain object from service-a with id: ${it.id}" }
            }
            .map { messageDomainObject ->
                DomainObject(
                    messageDomainObject.id,
                    messageDomainObject.fieldA,
                    messageDomainObject.fieldB
                )
            }
            .flatMap { domainObject ->
                domainObjectRepository.save(domainObject)
            }
            .doOnNext {
                logger.info { "Saved domain object with id: ${it.id}, fieldA: ${it.fieldA}, fieldB: ${it.fieldB}" }
            }
            .subscribe()
    }

    companion object : KLogging()
}
