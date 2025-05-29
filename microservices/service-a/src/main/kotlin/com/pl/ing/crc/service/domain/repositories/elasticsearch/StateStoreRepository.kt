package com.pl.ing.crc.service.domain.repositories.elasticsearch

import com.pl.ing.crc.service.domain.model.elasticsearch.EventDTO
import java.util.*
import org.springframework.data.elasticsearch.repository.ReactiveElasticsearchRepository

interface StateStoreRepository : ReactiveElasticsearchRepository<EventDTO, UUID>
