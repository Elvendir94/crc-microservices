package com.pl.ing.crc.service.infrastructure.configuration

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "spring.elasticsearch")
internal class ElasticsearchConfigurationProperties(
    val uris: List<String>,
    val username: String,
    val password: String
)
