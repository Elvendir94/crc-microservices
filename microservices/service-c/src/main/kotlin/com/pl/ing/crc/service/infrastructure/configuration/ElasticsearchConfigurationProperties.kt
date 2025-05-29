package com.pl.ing.crc.service.infrastructure.configuration

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "spring.elasticsearch")
internal class ElasticsearchConfigurationProperties(
    val uris: List<String>,
    val username: String,
    val password: String
) {
    // For compatibility with the configuration in ElasticsearchConfiguration
    val host: String
        get() = if (uris.isNotEmpty()) uris[0].substringBefore(":") else "localhost"
    
    val port: Int
        get() = if (uris.isNotEmpty() && uris[0].contains(":")) {
            uris[0].substringAfter(":").toIntOrNull() ?: 9200
        } else {
            9200
        }
}
