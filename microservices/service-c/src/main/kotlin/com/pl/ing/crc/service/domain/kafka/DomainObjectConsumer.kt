package com.pl.ing.crc.service.domain.kafka

import com.pl.ing.crc.service.domain.model.elasticsearch.DomainObject
import com.pl.ing.crc.service.domain.repositories.elasticsearch.DomainObjectRepository
import mu.KLogging
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class DomainObjectConsumer(
        private val domainObjectRepository: DomainObjectRepository
) {

    @KafkaListener(
            topics = ["processed-domain-object-topic"],
            groupId = "service-c-group",
            containerFactory = "kafkaListenerContainerFactory"
    )
    fun consume(domainObject: DomainObject) {
        logger.info { "Consuming DomainObject from Kafka: $domainObject" }
        domainObjectRepository.save(domainObject)
        logger.info { "DomainObject saved to Elasticsearch: ${domainObject.aggregateId}" }
    }

    companion object : KLogging()
}