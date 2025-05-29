package com.pl.ing.crc.service.domain.model.kafka

import com.fasterxml.jackson.annotation.JsonProperty

data class MessageToMicroB(
    @JsonProperty("messageId") var messageId: String,
    @JsonProperty("aggregateId") var aggregateId: String,
    @JsonProperty("fieldA") var fieldA: String,
    @JsonProperty("fieldB") var fieldB: String
)

