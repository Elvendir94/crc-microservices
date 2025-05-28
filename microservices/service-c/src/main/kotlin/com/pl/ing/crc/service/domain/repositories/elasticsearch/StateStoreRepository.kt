package com.pl.ing.crc.service.domain.repositories.elasticsearch

import com.pl.ing.crc.service.domain.model.elasticsearch.EventDTO
import org.springframework.data.elasticsearch.repository.ReactiveElasticsearchRepository
import java.util.*

interface StateStoreRepository :
    ReactiveElasticsearchRepository<EventDTO, UUID>
