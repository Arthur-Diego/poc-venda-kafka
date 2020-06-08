package com.poc.venda.repository;

import com.poc.venda.model.Carro;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

@Repository(value = "carroReposito")
public interface CarroRepository extends EntityBaseRepository<Carro, Long>{
}
