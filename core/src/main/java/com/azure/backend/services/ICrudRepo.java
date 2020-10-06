package com.azure.backend.services;

import com.azure.cosmos.models.SqlQuerySpec;
import com.azure.spring.data.cosmos.repository.ReactiveCosmosRepository;
import org.springframework.data.repository.NoRepositoryBean;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@NoRepositoryBean
public interface ICrudRepo<BaseDTO, String> extends ReactiveCosmosRepository<BaseDTO, String> {
}
