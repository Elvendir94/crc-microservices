package com.pl.ing.crc.service.domain.kafka

import com.pl.ing.crc.service.domain.model.elasticsearch.DomainObject
import com.pl.ing.crc.service.domain.repositories.elasticsearch.DomainObjectRepository
import mu.KLogging
import reactor.core.publisher.Flux
import java.util.function.Consumer

internal class MicroserviceCProcessor(
    private val domainObjectRepository: DomainObjectRepository,
) {
    fun process(): Consumer<Flux<DomainObject>> = Consumer { flux ->
        flux
            .doOnNext {
                logger.info { "Received DomainObject: $it" }
            }
            .flatMap { domainObject ->
                domainObjectRepository.save(domainObject)
            }
            .doOnNext {
                logger.info { "Saved DomainObject to DB: $it" }
            }
            .subscribe()
    }

    companion object : KLogging()
}
