package com.pl.ing.crc.service.infrastructure.configuration


import com.fasterxml.jackson.databind.ObjectMapper
import com.pl.ing.crc.service.domain.kafka.MicroserviceAtoCProcessor
import com.pl.ing.crc.service.domain.model.elasticsearch.DomainObject
import com.pl.ing.crc.service.domain.repositories.elasticsearch.DomainObjectRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.messaging.Message
import reactor.core.publisher.Flux
import java.util.function.Consumer

@Configuration
internal class ApplicationConfiguration {

    @Bean
    fun processDomainObjectFromA(
        domainObjectRepository: DomainObjectRepository,
        objectMapper: ObjectMapper
    ): Consumer<Flux<Message<DomainObject>>> {
        val processorDelegate = MicroserviceAtoCProcessor(domainObjectRepository, objectMapper)
        return processorDelegate.processDomainObjectFromA()
    }
}
