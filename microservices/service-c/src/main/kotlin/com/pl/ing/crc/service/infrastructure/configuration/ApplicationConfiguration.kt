package com.pl.ing.crc.service.infrastructure.configuration

import com.fasterxml.jackson.databind.ObjectMapper
import com.pl.ing.crc.service.domain.kafka.MicroserviceAResponseConsumer
import com.pl.ing.crc.service.domain.repositories.elasticsearch.DomainObjectRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.messaging.Message
import reactor.core.publisher.Flux
import java.util.function.Consumer

@Configuration
internal class ApplicationConfiguration {

    @Bean
    fun microserviceAConsumer(
        stateStoreRepository: DomainObjectRepository,
        objectMapper: ObjectMapper
    ): MicroserviceAResponseConsumer {
        return MicroserviceAResponseConsumer(stateStoreRepository, objectMapper)
    }

    @Bean
    fun microserviceAResponse(
        microserviceAResponseConsumer: MicroserviceAResponseConsumer
    ): Consumer<Flux<Message<Map<String, Any?>>>> {
        return microserviceAResponseConsumer.process()
    }
}
