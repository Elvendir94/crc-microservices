package com.pl.ing.crc.service.domain.kafka

import com.pl.ing.crc.service.domain.model.elasticsearch.DomainObject
import com.pl.ing.crc.service.domain.repositories.elasticsearch.DomainObjectRepository
import mu.KLogging
import org.springframework.messaging.Message
import reactor.core.publisher.Flux
import java.util.function.Function

class DomainObjectProcessor(
    private val domainObjectRepository: DomainObjectRepository
) {

    fun process(): Function<Flux<Message<DomainObject>>, Flux<Void>> = Function { input ->
        input
            .map { it.payload }
            .flatMap { domainObject ->
                logger.info { "Received DomainObject: $domainObject" }
                domainObjectRepository.save(domainObject).thenMany(Flux.empty<Void>())
            }
    }

    companion object : KLogging()
}