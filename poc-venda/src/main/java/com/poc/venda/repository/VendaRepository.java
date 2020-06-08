package com.poc.venda.repository;

import com.poc.venda.model.Venda;
import org.springframework.stereotype.Repository;


@Repository(value = "vendaReposito")
public interface VendaRepository extends EntityBaseRepository<Venda, Long>{
}
