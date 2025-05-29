package com.pl.ing.crc.service.domain.model.elasticsearch

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.data.elasticsearch.annotations.Document

@Document(indexName = "domain-object-index")
data class DomainObject(
    @JsonProperty("id") var id: String,
    @JsonProperty("fieldA") var fieldA: String,
    @JsonProperty("fieldB") var fieldB: String
)

