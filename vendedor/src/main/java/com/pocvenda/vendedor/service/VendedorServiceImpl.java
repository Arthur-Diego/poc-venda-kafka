package com.pocvenda.vendedor.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pocvenda.vendedor.domain.VendaEventIds;
import com.pocvenda.vendedor.domain.VendaEventObject;
import com.pocvenda.vendedor.kafka.producer.ProducerGeneric;
import com.pocvenda.vendedor.model.Vendedor;
import com.pocvenda.vendedor.repository.EntityBaseRepository;
import com.pocvenda.vendedor.repository.EntityBaseRepositoryImpl;
import com.pocvenda.vendedor.utils.StringUtilsTopics;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.RecoverableDataAccessException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.Optional;

@Service
@Slf4j
public class VendedorServiceImpl extends EntityBaseRepositoryImpl<Vendedor, Long>
        implements VendedorService{

    public VendedorServiceImpl(EntityBaseRepository<Vendedor, Long> entityBaseRepository) {
        super(entityBaseRepository);
    }
    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    KafkaTemplate<Integer,String> kafkaTemplate;

    @Autowired
    private VendedorService vendedorService;

    @Autowired
    private ProducerGeneric producerGeneric;

    @Override
    public void processVendaEvento(ConsumerRecord<Integer,String> consumerRecord) throws JsonProcessingException {
        VendaEventIds vendaEventIds = objectMapper.readValue(consumerRecord.value(), VendaEventIds.class);
        log.info("libraryEvent : {} ", vendaEventIds);

        if (vendaEventIds.getVendaEventId() != null && vendaEventIds.getVendaEventId() == 000) {
            throw new RecoverableDataAccessException("Temporary Network Issue");
        }

        Optional<Vendedor> vendedorReturn = vendedorService.findById(vendaEventIds.getVendedor());

        VendaEventObject vendaEventProducerObject = null;

        if (vendedorReturn.isPresent()) {

            Vendedor vendedor = vendedorReturn.get();

            vendaEventProducerObject = VendaEventObject.builder()
                    .carro(vendaEventIds.getCarro())
                    .idVendedor(vendedor.getIdVendedor())
                    .nome(vendedor.getNomeVendedor())
                    .vendaEventId(333).build();

        }
        producerGeneric.sendEvent(vendaEventProducerObject, StringUtilsTopics.TOPIC_VENDA_OBJECT);
    }

    @Override
    public void handleRecovery(ConsumerRecord<Integer,String> record){

        Integer key = record.key();
        String message = record.value();

        ListenableFuture<SendResult<Integer,String>> listenableFuture = kafkaTemplate.sendDefault(key, message);
        listenableFuture.addCallback(new ListenableFutureCallback<SendResult<Integer, String>>() {
            @Override
            public void onFailure(Throwable ex) {
                handleFailure(key, message, ex);
            }

            @Override
            public void onSuccess(SendResult<Integer, String> result) {
                handleSuccess(key, message, result);
            }
        });
    }

    private void handleFailure(Integer key, String value, Throwable ex) {
        log.error("Error Sending the Message and the exception is {}", ex.getMessage());
        try {
            throw ex;
        } catch (Throwable throwable) {
            log.error("Error in OnFailure: {}", throwable.getMessage());
        }
    }

    private void handleSuccess(Integer key, String value, SendResult<Integer, String> result) {
        log.info("Message VENDEDOR Sent SuccessFully for the key : {} and the value is {} , partition is {}", key, value, result.getRecordMetadata().partition());
    }
}
