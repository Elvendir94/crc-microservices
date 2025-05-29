package com.pl.ing.crc.service.infrastructure.configuration

import java.time.Duration
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.data.elasticsearch.client.ClientConfiguration
import org.springframework.data.elasticsearch.client.elc.ReactiveElasticsearchConfiguration

@Configuration
@EnableConfigurationProperties(ElasticsearchConfigurationProperties::class)
internal class ElasticsearchConfiguration(
    val elasticsearchProperties: ElasticsearchConfigurationProperties
) : ReactiveElasticsearchConfiguration() {

  override fun clientConfiguration(): ClientConfiguration {
    val clientBuilder = ClientConfiguration.builder().connectedTo(*buildElasticAddresses())

    return clientBuilder
        .withConnectTimeout(5000)
        .withSocketTimeout(Duration.ofSeconds(300))
        .withBasicAuth(elasticsearchProperties.username, elasticsearchProperties.password)
        .build()
  }

  private fun buildElasticAddresses() =
      elasticsearchProperties.uris.filterNot(String::isNullOrBlank).map(String::trim).toTypedArray()
}
