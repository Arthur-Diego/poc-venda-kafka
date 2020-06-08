package com.pocvenda.vendedor.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.pocvenda.vendedor.model.Vendedor;
import org.apache.kafka.clients.consumer.ConsumerRecord;

public interface VendedorService extends EntityBaseService<Vendedor, Long> {
    public void processVendaEvento(ConsumerRecord<Integer,String> consumerRecord) throws JsonProcessingException;
    public void handleRecovery(ConsumerRecord<Integer,String> record);
}
