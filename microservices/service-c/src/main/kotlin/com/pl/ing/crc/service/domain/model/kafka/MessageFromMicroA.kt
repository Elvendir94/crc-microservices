package com.pl.ing.crc.service.domain.model.kafka

import com.fasterxml.jackson.annotation.JsonProperty

data class MessageFromMicroA(
    @JsonProperty("fieldA") val fieldA: String,
    @JsonProperty("fieldB") val fieldB: String,
)
