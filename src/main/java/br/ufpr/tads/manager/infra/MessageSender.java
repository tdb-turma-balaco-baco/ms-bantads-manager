package br.ufpr.tads.manager.infra;

import br.ufpr.tads.manager.events.DomainEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MessageSender {
    private final RabbitTemplate rabbitTemplate;

    public void sendMessage(DomainEvent event) {
        rabbitTemplate.convertAndSend("manager-response.queue", event);
    }

}
