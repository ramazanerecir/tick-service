package com.rerecir.ticksserver.service.impl;

import com.rerecir.ticksserver.component.TickValidator;
import com.rerecir.ticksserver.component.TimestampValidator;
import com.rerecir.ticksserver.entity.dto.TickDto;
import com.rerecir.ticksserver.service.TickService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TickServiceImpl implements TickService {

    private final RabbitTemplate rabbitTemplate;
    private final TickValidator tickValidator;
    private final TimestampValidator timestampValidator;

    @Value("${rabbitmq.tick.exchange.name}")
    private String exchangeName;

    @Value("${rabbitmq.tick.routing.name}")
    private String routingName;

    @Override
    public boolean insertTick(TickDto tickDto) {
        if (tickValidator.validateTick(tickDto) &&
                timestampValidator.validateTimestamp(tickDto.getTimestamp())) {
            sendToTickQueue(tickDto);
            return true;
        }
        return false;
    }

    @Override
    public void sendToTickQueue(TickDto tickDto) {
        rabbitTemplate.convertAndSend(exchangeName, routingName, tickDto);
    }

}
