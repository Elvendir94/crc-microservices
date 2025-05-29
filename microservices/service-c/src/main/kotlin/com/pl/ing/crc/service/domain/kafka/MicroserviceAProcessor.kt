package com.pl.ing.crc.service.domain.kafka

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.convertValue
import com.pl.ing.crc.service.domain.model.elasticsearch.EventDTO
import com.pl.ing.crc.service.domain.model.kafka.MessageFromMicroA
import com.pl.ing.crc.service.domain.model.kafka.MessageToMicroA
import com.pl.ing.crc.service.domain.repositories.elasticsearch.StateStoreRepository
import mu.KLogging
import org.springframework.messaging.Message
import reactor.core.publisher.Flux
import java.time.Instant
import java.util.*
import java.util.function.Function

internal class MicroserviceAProcessor(
    private val stateStoreRepository: StateStoreRepository,
    private val objectMapper: ObjectMapper
) {

  fun process(): Function<Flux<Message<Map<String, Any?>>>, Flux<MessageToMicroA>> = Function { input ->
    input
        .map {
            message -> EventDTO(message.payload["messageId"] as String,
                                message.payload["aggregateId"] as String,
                                objectMapper.convertValue<MessageFromMicroA>(message.payload),
                                Instant.now().toEpochMilli())
        }
        .filter { true } // Add any filtering logic if needed
        .doOnNext {
            logger.info { "Put some processing here." }
        }
        .flatMap {
            stateStoreRepository.save(it)
        }.map {
            MessageToMicroA(UUID.randomUUID().toString(), it.aggregateId, "correctedValueForFieldA", "correctedValueForFieldB")
        }.doOnNext {
            logger.info { "Processed message correctly" }
        }
  }

  companion object : KLogging()
}
