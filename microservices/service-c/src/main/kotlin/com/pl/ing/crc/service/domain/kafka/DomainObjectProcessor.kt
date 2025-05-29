package com.pl.ing.crc.service.domain.kafka

import com.pl.ing.crc.service.domain.model.elasticsearch.DomainObject
import com.pl.ing.crc.service.domain.repositories.elasticsearch.DomainObjectRepository
import mu.KLogging
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.messaging.Message
import reactor.core.publisher.Flux
import java.util.function.Function

@Configuration
class DomainObjectProcessorConfig(private val domainObjectRepository: DomainObjectRepository) {
    companion object : KLogging()

    @Bean
    fun domainObjectProcessor(): Function<Flux<Message<DomainObject>>, Flux<Void>> = Function { input ->
        input
            .map { it.payload }
            .doOnNext { logger.info { "Received DomainObject: $it" } }
            .flatMap { domainObjectRepository.save(it) }
            .thenMany(Flux.empty())
    }
}
