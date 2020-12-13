package com.rerecir.ticksserver.service.impl;

import com.rerecir.ticksserver.entity.CalculationEvent;
import com.rerecir.ticksserver.enums.CalculationSource;
import com.rerecir.ticksserver.event.listener.TickEventCreated;
import com.rerecir.ticksserver.service.TickEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TickEventPublisherImpl implements TickEventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    @Async
    public void publish(String instrument, CalculationSource calculationSource) {
        applicationEventPublisher.publishEvent(new TickEventCreated(new CalculationEvent(instrument, calculationSource)));
    }
}
