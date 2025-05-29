package com.pl.ing.crc.service.infrastructure.configuration

import java.time.Duration
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.data.elasticsearch.client.ClientConfiguration
import org.springframework.data.elasticsearch.client.elc.ReactiveElasticsearchConfiguration
import org.springframework.data.elasticsearch.repository.config.EnableReactiveElasticsearchRepositories

@Configuration
@EnableConfigurationProperties(ElasticsearchConfigurationProperties::class)
@EnableReactiveElasticsearchRepositories(basePackages = ["com.pl.ing.crc.service.domain.repositories.elasticsearch"])
internal class ElasticsearchConfiguration(
    val elasticsearchProperties: ElasticsearchConfigurationProperties
) : ReactiveElasticsearchConfiguration() {

    override fun clientConfiguration(): ClientConfiguration {
        val clientBuilder = ClientConfiguration.builder()
            .connectedTo(elasticsearchProperties.host + ":" + elasticsearchProperties.port)

        return clientBuilder
            .withConnectTimeout(5000)
            .withSocketTimeout(Duration.ofSeconds(300))
            .withBasicAuth(elasticsearchProperties.username, elasticsearchProperties.password)
            .build()
    }
}
