package com.poc.venda.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.poc.venda.domain.VendaEventObject;
import com.poc.venda.domain.VendaEventObject;
import com.poc.venda.model.Carro;
import com.poc.venda.model.Venda;
import com.poc.venda.repository.EntityBaseRepository;
import com.poc.venda.repository.EntityBaseRepositoryImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.RecoverableDataAccessException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.Optional;

@Service
@Slf4j
public class VendaServiceImplEvent extends EntityBaseRepositoryImpl<Venda, Long>
        implements VendaService{

    public VendaServiceImplEvent(EntityBaseRepository<Venda, Long> entityBaseRepository) {
        super(entityBaseRepository);
    }

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    KafkaTemplate<Integer,String> kafkaTemplate;

    @Autowired
    private VendaService vendaService;

    @Autowired
    private CarroService carroService;

    @Override
    public void processVendaEvento(ConsumerRecord<Integer,String> consumerRecord) throws JsonProcessingException {
        VendaEventObject vendaEventObject = objectMapper.readValue(consumerRecord.value(), VendaEventObject.class);
        log.info("libraryEvent : {} ", vendaEventObject);

        if(vendaEventObject.getVendaEventId()!=null && vendaEventObject.getVendaEventId()==000){
            throw new RecoverableDataAccessException("Temporary Network Issue");
        }

        Venda venda = null;
        Optional<Carro> carroReturn = carroService.findById(vendaEventObject.getCarro());

        if(carroReturn.isPresent()){
            venda = Venda.builder()
                    .idVendedor(vendaEventObject.getIdVendedor())
                    .nomeVendedor(vendaEventObject.getNome())
                    .carro(carroReturn.get()).build();
        }

        this.saveVenda(venda);
    }


    private void saveVenda(Venda venda){
        vendaService.save(venda);
        log.info("Successfully Persisted the VENDA Event {} ", venda);
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
        log.info("Message VENDA DO CARRO Sent SuccessFully for the key : {} and the value is {} , partition is {}", key, value, result.getRecordMetadata().partition());
    }
}
