package com.pl.ing.crc.service.infrastructure.configuration


import com.fasterxml.jackson.databind.ObjectMapper
import com.pl.ing.crc.service.domain.kafka.DomainObjectProcessor
import com.pl.ing.crc.service.domain.kafka.MicroserviceBResponseConsumer
import com.pl.ing.crc.service.domain.kafka.WebRequestProcessor
import com.pl.ing.crc.service.domain.model.elasticsearch.DomainObject
import com.pl.ing.crc.service.domain.model.kafka.MessageToMicroB
import com.pl.ing.crc.service.domain.repositories.elasticsearch.DomainObjectRepository
import com.pl.ing.crc.service.domain.repositories.elasticsearch.StateStoreRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.messaging.Message
import reactor.core.publisher.Flux
import java.util.function.Consumer
import java.util.function.Function

@Configuration
internal class ApplicationConfiguration {

    @Bean
    fun domainObjectProcessor(
        stateStoreRepository: StateStoreRepository,
        objectMapper: ObjectMapper
    ): DomainObjectProcessor {
        return DomainObjectProcessor(stateStoreRepository, objectMapper)
    }

    @Bean
    fun processDomainObject(
        domainObjectProcessor: DomainObjectProcessor
    ): Function<Flux<Message<Map<String, Any?>>>, Flux<Message<DomainObject>>> {
        return domainObjectProcessor
    }
}
