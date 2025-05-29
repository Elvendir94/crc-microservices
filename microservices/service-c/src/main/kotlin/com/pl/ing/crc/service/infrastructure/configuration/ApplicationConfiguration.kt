package com.pl.ing.crc.service.infrastructure.configuration

import java.util.UUID
import com.pl.ing.crc.service.domain.kafka.MicroserviceASaveRequestConsumer
import com.fasterxml.jackson.databind.ObjectMapper
//import com.pl.ing.crc.service.domain.kafka.MicroserviceBResponseConsumer
//import com.pl.ing.crc.service.domain.kafka.WebRequestProcessor
//import com.pl.ing.crc.service.domain.model.kafka.MessageToMicroB
import com.pl.ing.crc.service.domain.repositories.elasticsearch.DomainObjectRepository
//import com.pl.ing.crc.service.domain.repositories.elasticsearch.StateStoreRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.messaging.Message
import reactor.core.publisher.Flux
import java.util.function.Consumer
import java.util.function.Function

@Configuration
internal class ApplicationConfiguration {

    @Bean
    fun microserviceASaveRequestConsumer(
        domainObjectRepository: DomainObjectRepository,
        objectMapper: ObjectMapper
    ): MicroserviceASaveRequestConsumer {
        return MicroserviceASaveRequestConsumer(domainObjectRepository, objectMapper)
    }

    @Bean
    fun microserviceASaveRequest(
        microserviceASaveRequestConsumer: MicroserviceASaveRequestConsumer
    ): Consumer<Flux<Message<Map<String, Any?>>>> {
        return microserviceASaveRequestConsumer.process()
    }

//    @Bean
//    fun microserviceBResponseConsumer(
//        stateStoreRepository: StateStoreRepository,
//        domainObjectRepository: DomainObjectRepository,
//        objectMapper: ObjectMapper
//    ): MicroserviceBResponseConsumer {
//        return MicroserviceBResponseConsumer(stateStoreRepository, domainObjectRepository, objectMapper)
//    }

//    @Bean
//    fun microserviceBResponse(
//        microserviceBResponseConsumer: MicroserviceBResponseConsumer
//    ): Consumer<Flux<Message<Map<String, Any?>>>> {
//        return microserviceBResponseConsumer.process()
//    }
//
//    @Bean
//    fun webRequestProcessor(
//        stateStoreRepository: StateStoreRepository,
//        objectMapper: ObjectMapper
//    ): WebRequestProcessor {
//        return WebRequestProcessor(stateStoreRepository, objectMapper)
//    }
//
//    @Bean
//    fun webRequest(
//        webRequestProcessor: WebRequestProcessor
//    ): Function<Flux<Message<Map<String, Any?>>>, Flux<MessageToMicroB>> {
//        return webRequestProcessor.process()
//    }
}
