package com.pl.ing.crc.service.infrastructure.configuration

import com.fasterxml.jackson.databind.ObjectMapper
import com.pl.ing.crc.service.domain.kafka.ServiceADomainObjectConsumer
import com.pl.ing.crc.service.domain.repositories.elasticsearch.DomainObjectRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.messaging.Message
import reactor.core.publisher.Flux
import java.util.function.Consumer

@Configuration
internal class ApplicationConfiguration {

    @Bean
    fun serviceADomainObjectConsumer(
        domainObjectRepository: DomainObjectRepository,
        objectMapper: ObjectMapper
    ): ServiceADomainObjectConsumer {
        return ServiceADomainObjectConsumer(domainObjectRepository, objectMapper)
    }

    @Bean
    fun serviceADomainObject(
        serviceADomainObjectConsumer: ServiceADomainObjectConsumer
    ): Consumer<Flux<Message<Map<String, Any?>>>> {
        return serviceADomainObjectConsumer.process()
    }
}
