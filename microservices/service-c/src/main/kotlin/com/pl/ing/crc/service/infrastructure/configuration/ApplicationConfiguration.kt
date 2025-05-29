package com.pl.ing.crc.service.infrastructure.configuration


import com.fasterxml.jackson.databind.ObjectMapper
import com.pl.ing.crc.service.domain.kafka.MicroserviceCProcessor
import com.pl.ing.crc.service.domain.repositories.elasticsearch.DomainObjectRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.messaging.Message
import reactor.core.publisher.Flux
import java.util.function.Function

@Configuration
class ApplicationConfiguration {

    @Bean
    fun microserviceCProcessor(domainObjectRepository: DomainObjectRepository) =
            MicroserviceCProcessor(domainObjectRepository)
}
