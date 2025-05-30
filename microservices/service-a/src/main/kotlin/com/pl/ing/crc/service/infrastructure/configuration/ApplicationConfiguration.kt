package com.pl.ing.crc.service.infrastructure.configuration


import com.fasterxml.jackson.databind.ObjectMapper
import com.pl.ing.crc.service.domain.kafka.MicroserviceCProcessor
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
    fun microserviceBResponseProcessor(
        stateStoreRepository: StateStoreRepository,
        domainObjectRepository: DomainObjectRepository,
        objectMapper: ObjectMapper
    ): MicroserviceCProcessor {
        return MicroserviceCProcessor(stateStoreRepository, domainObjectRepository, objectMapper)
    }

    @Bean
    fun microserviceBResponse(
        microserviceCProcessor: MicroserviceCProcessor
    ): Function<Flux<Message<Map<String, Any?>>>, Flux<DomainObject>> {
        return microserviceCProcessor.process()
    }

    @Bean
    fun webRequestProcessor(
        stateStoreRepository: StateStoreRepository,
        objectMapper: ObjectMapper
    ): WebRequestProcessor {
        return WebRequestProcessor(stateStoreRepository, objectMapper)
    }

    @Bean
    fun webRequest(
        webRequestProcessor: WebRequestProcessor
    ): Function<Flux<Message<Map<String, Any?>>>, Flux<MessageToMicroB>> {
        return webRequestProcessor.process()
    }
}
