package com.pl.ing.crc.service.domain.repositories.elasticsearch

import com.pl.ing.crc.service.domain.model.elasticsearch.DomainObject
import java.util.*
import org.springframework.data.elasticsearch.repository.ReactiveElasticsearchRepository

interface DomainObjectRepository :
        ReactiveElasticsearchRepository<DomainObject, String>