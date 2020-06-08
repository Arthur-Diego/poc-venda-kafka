package com.pocvenda.vendedor.kafka.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.pocvenda.vendedor.service.VendedorServiceImpl;
import com.pocvenda.vendedor.utils.StringUtilsTopics;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class VendaEventsIdsConsumer {

    @Autowired
    private VendedorServiceImpl vendedorEventsService;

    @KafkaListener(topics = {StringUtilsTopics.TOPIC_VENDA_IDS})
    public void onMessage(ConsumerRecord<Integer,String> consumerRecord) throws JsonProcessingException {
        log.info("ConsumerRecord : {} ", consumerRecord );
        vendedorEventsService.processVendaEvento(consumerRecord);
    }
}
