package br.net.dac.manager.Infrastructure.Services.Messaging.Configuration;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.DefaultClassMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;

import br.net.dac.manager.Application.Services.Manager.Events.ChangeManagerClients;
import br.net.dac.manager.Application.Services.Manager.Events.RemoveManagerEvent;
import br.net.dac.manager.Application.Services.Manager.Events.SaveManagerEvent;
import br.net.dac.manager.Application.Services.Manager.Events.SelectMaxClientEvent;
import br.net.dac.manager.Application.Services.Manager.Events.SelectMinClientEvent;
import br.net.dac.manager.Application.Services.Manager.Events.UpdateManagerEvent;
import br.net.dac.manager.Domain.Events.ChangedManagerClientsEvent;
import br.net.dac.manager.Domain.Events.CreatedManagerEvent;
import br.net.dac.manager.Domain.Events.SelectedManagerEvent;
import br.net.dac.manager.Domain.Events.SelectedTopEvent;
import br.net.dac.manager.Domain.Events.UpdatedManagerEvent;

@Configuration
public class RabbitConfig {
    @Value("${manager.queue}")
    private String managerQueue;

    @Value("${manager-response.queue}")
    private String managerResponseQueue;

    @Bean
    public Queue managerQueue() {
        return new Queue(managerQueue, true);
    }

    @Bean
    public Queue managerRespnseQueue() {
        return new Queue(managerResponseQueue, true);
    }


    @Bean
    public MessageConverter messageConverter() {
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();
        converter.setClassMapper(classMapper());
        return converter;
    }

    @Bean
    public DefaultClassMapper classMapper() {
        DefaultClassMapper classMapper = new DefaultClassMapper();
        classMapper.setIdClassMapping(customClassMapping());
        classMapper.setTrustedPackages("*");
        return classMapper;
    }

    public Map<String, Class<?>> customClassMapping(){
        Map<String, Class<?>> idClassMapping = new HashMap<>();
        idClassMapping.put("CreateManagerEvent", SaveManagerEvent.class);
        idClassMapping.put("RemoveManagerEvent", RemoveManagerEvent.class);
        idClassMapping.put("UpdateManagerEvent", UpdateManagerEvent.class);

        idClassMapping.put("ChangeManagerClients", ChangeManagerClients.class);
        idClassMapping.put("SelectMaxCientEvent", SelectMaxClientEvent.class);
        idClassMapping.put("SelectMinClientEvent", SelectMinClientEvent.class);

        idClassMapping.put("ChangedManagerClientsEvent", ChangedManagerClientsEvent.class);
        idClassMapping.put("CreatedManagerEvent", CreatedManagerEvent.class);
        idClassMapping.put("SelectedManagerEvent", SelectedManagerEvent.class);
        idClassMapping.put("SelectedTopEvent", SelectedTopEvent.class);
        idClassMapping.put("UpdatedManagerEvent", UpdatedManagerEvent.class);
        
        return idClassMapping;
    }


    public AmqpTemplate template(ConnectionFactory connection) {
        RabbitTemplate template = new RabbitTemplate(connection);
        template.setMessageConverter(messageConverter());
        return template;
    }
}
