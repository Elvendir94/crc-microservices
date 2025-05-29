package com.pl.ing.crc.service.infrastructure.configuration


import com.fasterxml.jackson.databind.ObjectMapper
import com.pl.ing.crc.service.domain.kafka.MicroserviceAResponseConsumer
import com.pl.ing.crc.service.domain.model.elasticsearch.DomainObject
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
    fun microserviceAResponseConsumer(
            stateStoreRepository: StateStoreRepository,
            domainObjectRepository: DomainObjectRepository,
            objectMapper: ObjectMapper
    ): MicroserviceAResponseConsumer {
        return MicroserviceAResponseConsumer(stateStoreRepository, domainObjectRepository, objectMapper)
    }
    @Bean
    fun microserviceAResponseConsumerInput(microserviceAResponseConsumer: MicroserviceAResponseConsumer): Consumer<Flux<Message<Map<String, Any?>>>> {
        return microserviceAResponseConsumer.process()
    }
}
