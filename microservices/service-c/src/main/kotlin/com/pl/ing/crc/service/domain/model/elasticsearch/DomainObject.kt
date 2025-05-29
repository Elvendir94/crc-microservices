package com.pl.ing.crc.service.domain.model.elasticsearch

import org.springframework.data.annotation.Id
import org.springframework.data.elasticsearch.annotations.Document

@Document(indexName = "domain-object-index")
data class DomainObject(
    @Id val id: String,
    val fieldA: String,
    val fieldB: String
)
