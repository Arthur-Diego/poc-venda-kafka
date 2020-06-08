package com.pocvenda.vendedor.config.topic;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class AutoConfigTopic {

    @Bean
    public NewTopic vendaId(){
        return TopicBuilder.name("vendas-producer-object")
                .partitions(1)
                .replicas(1)
                .build();
    }

}