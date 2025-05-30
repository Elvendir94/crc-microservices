package com.pl.ing.crc.service.domain.kafka

import com.pl.ing.crc.service.domain.model.elasticsearch.DomainObject
import com.pl.ing.crc.service.domain.repositories.elasticsearch.DomainObjectRepository
import mu.KLogging
import reactor.core.publisher.Flux
import java.util.function.Consumer

internal class MicroserviceCProcessor(
    private val domainObjectRepository: DomainObjectRepository
) {

    fun process(): Consumer<Flux<DomainObject>> = Consumer { flux ->
        flux
            .doOnNext { logger.info { "Received DomainObject: $it" } }
            .flatMap { domainObjectRepository.save(it) }
            .doOnNext { logger.info { "Saved DomainObject to Elasticsearch: $it" } }
            .doOnError { ex -> logger.error(ex) { "Error while processing DomainObject" } }
            .onErrorContinue { ex, obj -> logger.warn(ex) { "Continuing after error on $obj" } }
            .subscribe()
    }

    companion object : KLogging()
}
