package com.pl.ing.crc.service.domain.model.elasticsearch

import com.pl.ing.crc.service.domain.model.kafka.MessageFromMicroA
import org.springframework.data.annotation.Id
import org.springframework.data.elasticsearch.annotations.Document

@Document(indexName = "event-store-service-c")
data class EventDTO(
    @Id val messageId: String,
    val aggregateId: String,
    val eventBody: MessageFromMicroA,
    val timestamp: Long
)