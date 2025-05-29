package com.pl.ing.crc.service.domain.kafka

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.convertValue
import com.pl.ing.crc.service.domain.model.elasticsearch.DomainObject
import com.pl.ing.crc.service.domain.repositories.elasticsearch.DomainObjectRepository
import mu.KLogging
import org.springframework.messaging.Message
import reactor.core.publisher.Flux
import java.util.function.Consumer

internal class MicroserviceAtoCProcessor(
    private val domainObjectRepository: DomainObjectRepository,
    private val objectMapper: ObjectMapper
) {

    fun processDomainObjectFromA(): Consumer<Flux<Message<DomainObject>>> = Consumer { input ->
        input
            .map { message ->
                logger.info { "Otrzymano Obiekt z service-a"}
                objectMapper.convertValue<DomainObject>(message.payload)
            }
            .doOnNext { domainObject ->
                logger.info { "Otrzymano DomainObject z service-a. MessageId: ${domainObject.id}, fieldA: ${domainObject.fieldA}, fieldB: ${domainObject.fieldB}" }
            }
            .flatMap{ domainObject -> domainObjectRepository.save(domainObject)
            }
            .doOnError { error ->
                logger.error(error) { "Błąd podczas przetwarzania DomainObject z service-a: " + error.message}
            }
            .doOnComplete {
                logger.info { "Zakończono przetwarzanie strumienia DomainObject z service-a." }
            }
            .subscribe()
    }

    companion object : KLogging()
}