package br.net.dac.manager.Application.Abstractions;

import br.net.dac.manager.Domain.Events.Common.DomainEvent;

public interface IMessageSender {
    void sendMessage(DomainEvent event);
}
