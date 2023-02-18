package br.net.dac.manager.Infrastructure.Services.Messaging;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.net.dac.manager.Application.Abstractions.IMessageSender;
import br.net.dac.manager.Domain.Events.DomainEvent;

@Component
public class MessageSender implements IMessageSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public void sendMessage(DomainEvent event) {
        rabbitTemplate.convertAndSend("manager-response.queue", event);        
    }
    
}
