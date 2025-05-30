package com.pl.ing.crc.service.domain.model.kafka

import com.fasterxml.jackson.annotation.JsonProperty

data class MessageToMicroA(
    @JsonProperty("messageId") val messageId: String,
    @JsonProperty("aggregateId") val aggregateId: String,
    @JsonProperty("fieldA") val fieldA: String,
    @JsonProperty("fieldB") val fieldB: String
)