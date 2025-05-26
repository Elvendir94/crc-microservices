package com.pl.ing.crc.service.infrastructure.configuration


import com.fasterxml.jackson.databind.ObjectMapper
import com.pl.ing.crc.service.domain.model.kafka.MessageToMicroA
import com.pl.ing.crc.service.domain.repositories.elasticsearch.StateStoreRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.messaging.Message
import reactor.core.publisher.Flux
import java.util.function.Function

@Configuration
internal class ApplicationConfiguration {

    @Bean
    fun microserviceAProcessor(
        stateStoreRepository: StateStoreRepository,
        objectMapper: ObjectMapper
    ): MicroserviceAProcessor {
        return MicroserviceAProcessor(stateStoreRepository, objectMapper)
    }

    @Bean
    fun microserviceARequest(
        microserviceAProcessor: MicroserviceAProcessor
    ): Function<Flux<Message<Map<String, Any?>>>, Flux<MessageToMicroA>> {
        return microserviceAProcessor.process()
    }
}
