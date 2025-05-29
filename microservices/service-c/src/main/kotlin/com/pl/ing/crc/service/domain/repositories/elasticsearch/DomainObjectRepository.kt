package com.pl.ing.crc.service.domain.repositories.elasticsearch

import com.pl.ing.crc.service.domain.model.elasticsearch.DomainObject
import org.springframework.data.elasticsearch.repository.ReactiveElasticsearchRepository
import org.springframework.stereotype.Repository

@Repository
interface DomainObjectRepository : ReactiveElasticsearchRepository<DomainObject, String>

