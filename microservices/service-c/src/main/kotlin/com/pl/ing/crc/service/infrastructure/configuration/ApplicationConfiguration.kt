package com.pl.ing.crc.service.infrastructure.configuration


import com.pl.ing.crc.service.domain.kafka.MicroserviceCProcessor
import com.pl.ing.crc.service.domain.model.elasticsearch.DomainObject
import com.pl.ing.crc.service.domain.repositories.elasticsearch.DomainObjectRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import reactor.core.publisher.Flux
import java.util.function.Consumer

@Configuration
internal class ApplicationConfiguration {

    @Bean
    fun microserviceCProcessor(
        domainObjectRepository: DomainObjectRepository,
    ): MicroserviceCProcessor {
        return MicroserviceCProcessor(domainObjectRepository)
    }

    @Bean
    fun microserviceARequest(
        microserviceCProcessor: MicroserviceCProcessor
    ) : Consumer<Flux<DomainObject>> {
        return microserviceCProcessor.process()
    }
}
