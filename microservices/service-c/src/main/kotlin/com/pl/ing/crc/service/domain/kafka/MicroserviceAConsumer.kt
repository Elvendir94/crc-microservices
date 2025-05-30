package com.pl.ing.crc.service.domain.kafka

import com.pl.ing.crc.service.domain.model.elasticsearch.DomainObject
import com.pl.ing.crc.service.domain.repositories.elasticsearch.DomainObjectRepository
import reactor.core.publisher.Flux
import java.util.function.Consumer

internal class MicroserviceAConsumer (
    private val domainObjectRepository: DomainObjectRepository
) {
    fun process(): Consumer<Flux<DomainObject>> = Consumer {input ->
        input
            .doOnNext {
                logger.info{ "Received domain object" }
            }
            .flatMap { domainObject ->
                domainObjectRepository.save(domainObject)
            }
            .subscribe()

    }

    companion object : KLogging()
}