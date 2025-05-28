package com.pl.ing.crc.service.infrastructure.configuration

import com.pl.ing.crc.service.domain.kafka.DomainObjectProcessor
import com.pl.ing.crc.service.domain.model.elasticsearch.DomainObject
import com.pl.ing.crc.service.domain.repositories.elasticsearch.DomainObjectRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.messaging.Message
import reactor.core.publisher.Flux
import java.util.function.Function

@Configuration
internal class ApplicationConfiguration {

    @Bean
    fun domainObjectProcessor(
        domainObjectRepository: DomainObjectRepository
    ): Function<Flux<Message<DomainObject>>, Flux<Void>> {
        return DomainObjectProcessor(domainObjectRepository).process()
    }
}
