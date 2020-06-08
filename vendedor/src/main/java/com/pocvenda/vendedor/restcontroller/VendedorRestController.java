package com.pocvenda.vendedor.restcontroller;

import com.pocvenda.vendedor.kafka.producer.ProducerGeneric;
import com.pocvenda.vendedor.model.Vendedor;
import com.pocvenda.vendedor.service.VendedorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("v1/vendedor")
public class VendedorRestController {

    @Autowired
    private VendedorService vendedorService;

    @Autowired
    private ProducerGeneric producerGeneric;

//    @PostMapping(value = "/vender")
//    public ResponseEntity<Object> venda(@RequestBody VendaEventIds vendaEventIds) throws JsonProcessingException {
//        vendaEventIds.setVendaEventType(VendaEventType.NEW);
//        producerGeneric.sendEvent(vendaEventIds, StringUtilsTopics.TOPIC_VENDA_IDS);
//        return ResponseEntity.status(HttpStatus.CREATED).body(vendaEventIds);
//    }

    @PostMapping
    public ResponseEntity<Vendedor> save(@RequestBody Vendedor vendedor){
        return ResponseEntity.ok(vendedorService.save(vendedor));
    }

    @PutMapping
    public ResponseEntity<Vendedor> update(@RequestBody Vendedor vendedor){
        return ResponseEntity.ok(vendedorService.save(vendedor));
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Optional<Vendedor>> findById(@PathVariable("id") Long id){
        return ResponseEntity.ok(vendedorService.findById(id));
    }

    @GetMapping
    public ResponseEntity<Iterable<Vendedor>> findAll(){
        return ResponseEntity.ok().body(vendedorService.findAll());
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id){
        vendedorService.deleteById(id);
        return ResponseEntity.ok().build();
    }

}
