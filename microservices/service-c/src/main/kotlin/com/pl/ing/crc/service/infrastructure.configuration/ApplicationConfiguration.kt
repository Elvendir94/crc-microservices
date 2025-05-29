package com.pl.ing.crc.service.infrastructure.configuration

import com.fasterxml.jackson.databind.ObjectMapper
import com.pl.ing.crc.service.domain.kafka.MicroserviceARequestConsumer
import com.pl.ing.crc.service.domain.repositories.elasticsearch.DomainObjectRepository
import com.pl.ing.crc.service.domain.repositories.elasticsearch.StateStoreRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.messaging.Message
import reactor.core.publisher.Flux
import java.util.function.Consumer

@Configuration
internal class ApplicationConfiguration
 {

    @Bean
    fun microserviceARequestConsumer(
        stateStoreRepository: StateStoreRepository,
        domainObjectRepository: DomainObjectRepository,
        objectMapper: ObjectMapper
    ): MicroserviceARequestConsumer {
        return MicroserviceARequestConsumer(stateStoreRepository, domainObjectRepository, objectMapper)
    }

     @Bean
     fun microserviceARequest(
         microserviceARequestConsumer: MicroserviceARequestConsumer
     ): Consumer<Flux<Message<Map<String, Any?>>>> {
         return microserviceARequestConsumer.process()
     }

}