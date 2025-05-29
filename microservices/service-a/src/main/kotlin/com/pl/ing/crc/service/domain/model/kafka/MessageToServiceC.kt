package com.pl.ing.crc.service.domain.model.kafka

import com.fasterxml.jackson.annotation.JsonProperty

data class MessageToServiceC(
    @JsonProperty("id") val id: String,
    @JsonProperty("fieldA") val fieldA: String,
    @JsonProperty("fieldB") val fieldB: String
)
