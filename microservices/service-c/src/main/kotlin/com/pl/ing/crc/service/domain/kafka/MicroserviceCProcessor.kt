package com.pl.ing.crc.service.domain.kafka

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.convertValue
import com.pl.ing.crc.service.domain.model.elasticsearch.DomainObject
import com.pl.ing.crc.service.domain.repositories.elasticsearch.DomainObjectRepository
import mu.KLogging
import org.springframework.messaging.Message
import reactor.core.publisher.Flux
import java.time.Instant
import java.util.*
import java.util.function.Consumer

class MicroserviceCProcessor(
    private val domainObjectRepository: DomainObjectRepository
) {

  fun saveDomainObjectProcessor(): Consumer<Flux<Message<DomainObject>>> = Consumer { flux ->
    flux
        .map { it.payload }
        .flatMap { domainObjectRepository.save(it) }
        .doOnNext { logger.info { "Saved message correctly" } }
        .subscribe()
  }

  companion object : KLogging()
}
