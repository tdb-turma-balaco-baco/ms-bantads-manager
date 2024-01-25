package br.ufpr.tads.manager;

import br.ufpr.tads.manager.events.*;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@RabbitListener(queues = {"${manager.queue}"})
public class ManagerConsumers {
    private final ManagerService service;

    @RabbitHandler
    public void receiveSave(@Payload SaveManagerEvent event) {
        service.saveManager(event);
    }

    @RabbitHandler
    public void receiveUpdate(@Payload UpdateManagerEvent event) {
        service.updateManager(event);
    }

    @RabbitHandler
    public void receiveRemove(@Payload RemoveManagerEvent event) {
        service.removeManager(event);
    }

    @RabbitHandler
    public void receiveChangeManagerClients(@Payload ChangeManagerClients event) {
        service.changeManagerClients(event);
    }

    @RabbitHandler
    public void receiveMinClients(@Payload SelectMinClientEvent event) {
        service.selectManagerMinClient(event);
    }

    @RabbitHandler
    public void receiveRemove(@Payload SelectMaxClientEvent event) {
        service.selectManagerMaxClient(event);
    }
}
