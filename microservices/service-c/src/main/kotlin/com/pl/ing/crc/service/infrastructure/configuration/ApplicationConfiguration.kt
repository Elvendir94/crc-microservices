package com.pl.ing.crc.service.infrastructure.configuration


import com.fasterxml.jackson.databind.ObjectMapper
import com.pl.ing.crc.service.domain.kafka.MicroserviceAConsumer
import com.pl.ing.crc.service.domain.model.elasticsearch.DomainObject
import com.pl.ing.crc.service.domain.model.kafka.MessageToMicroA
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
    fun microserviceAConsumer(
        domainObjectRepository: DomainObjectRepository
    ): MicroserviceAConsumer {
        return MicroserviceAConsumer(domainObjectRepository)
    }

    @Bean
    fun microserviceARequest(
        microserviceAConsumer: MicroserviceAConsumer
    ): Consumer<Flux<DomainObject>> {
        return microserviceAConsumer.process()
    }
}
