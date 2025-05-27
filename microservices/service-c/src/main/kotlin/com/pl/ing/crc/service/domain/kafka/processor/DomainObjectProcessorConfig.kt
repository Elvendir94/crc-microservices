package com.pl.ing.crc.service.domain.kafka

import com.pl.ing.crc.service.domain.model.elasticsearch.DomainObject
import com.pl.ing.crc.service.domain.model.kafka.MessageToMicroB
import com.pl.ing.crc.service.domain.repositories.elasticsearch.DomainObjectRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.function.Function
import mu.KLogging
import reactor.core.publisher.Flux
import org.springframework.messaging.Message

@Configuration
class DomainObjectProcessorConfig(
    private val domainObjectRepository: DomainObjectRepository
) {

    @Bean
    fun processDomainObject(): Function<Flux<Message<MessageToMicroB>>, Flux<DomainObject>> {
        return Function { input ->
            input
                .doOnNext { logger.info { "Received message with payload: ${it.payload}" } }
                .map { message ->
                    val payload = message.payload
                    val domainObject = DomainObject(
                        id = payload.aggregateId,
                        fieldA = payload.fieldA,
                        fieldB = payload.fieldB
                    )
                    logger.info { "Mapped MessageToMicroB to DomainObject: $domainObject" }
                    domainObject
                }
                .flatMap { domainObject ->
                    logger.info { "Saving DomainObject with ID: ${domainObject.id}" }
                    domainObjectRepository.save(domainObject)
                }
                .doOnNext {
                    logger.info { "Successfully saved DomainObject with ID: ${it.id}" }
                }
                .doOnError {
                    logger.error(it) { "Failed to process and save DomainObject" }
                }
        }
    }

    companion object : KLogging()
}
