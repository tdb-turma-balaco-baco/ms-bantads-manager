package br.net.dac.manager.Infrastructure.Consumers;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import br.net.dac.manager.Application.Services.Manager.IManagerService;
import br.net.dac.manager.Application.Services.Manager.Events.RemoveManagerEvent;
import br.net.dac.manager.Application.Services.Manager.Events.SaveManagerEvent;
import br.net.dac.manager.Application.Services.Manager.Events.UpdateManagerEvent;

@Component
@RabbitListener(queues = {"${manager.queue}"})
public class ManagerConsumers {
    
    @Autowired
    IManagerService _service;

    @RabbitHandler
    public void receiveSave(@Payload SaveManagerEvent event){
        _service.saveManager(event);
    }

    @RabbitHandler
    public void receiveUpdate(@Payload UpdateManagerEvent event){
        _service.updateManager(event);
    }

    @RabbitHandler
    public void receiveRemove(@Payload RemoveManagerEvent event){
        _service.removeManager(event);
    }
}
