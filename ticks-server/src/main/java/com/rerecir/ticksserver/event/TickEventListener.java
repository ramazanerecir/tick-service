package com.rerecir.ticksserver.event;

import com.rerecir.common.enums.CalculationType;
import com.rerecir.ticksserver.entity.CalculationEvent;
import com.rerecir.ticksserver.entity.InstrumentTick;
import com.rerecir.ticksserver.enums.CalculationSource;
import com.rerecir.ticksserver.event.listener.TickEventCreated;
import com.rerecir.ticksserver.repository.TickRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TickEventListener {

    private final TickRepository tickRepository;
    private final StatisticsRepository statisticsRepository;

    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.calculation.exchange.name}")
    private String exchangeName;

    @Value("${rabbitmq.calculation.routing.name}")
    private String routingName;

    @EventListener
    public void listenTickEvent(TickEventCreated tickEventCreated) {

        CalculationEvent calculationEvent = (CalculationEvent) tickEventCreated.getSource();

        sendToCalculationQueue(calculationEvent);
    }

    public void sendToCalculationQueue(CalculationEvent calculationEvent) {

        InstrumentTick instrumentTick;

        if (calculationEvent.getInstrument().equals("*")) {
            instrumentTick = tickRepository.getFilteredAllTicks();
        } else {
            instrumentTick = tickRepository.getFilteredInstrumentTick(calculationEvent.getInstrument());
        }

        Statistics statistics = statisticsRepository.get(calculationEvent.getInstrument());

        if (instrumentTick.getTickList().isEmpty() &&
                (statistics == null || statistics.getCount() == 0)) {
            //No need to calculation in such a case :
            // statistics is empty and no ticks, so no need to send instrument to calculation queue
            return;
        }

        if (calculationEvent.getCalculationSource() == CalculationSource.SCHEDULED &&
                !instrumentTick.getTickList().isEmpty() &&
                statistics != null &&
                statistics.getCount() == instrumentTick.getTickList().size() &&
                statistics.getInstrumentUpdatedAt() == instrumentTick.getUpdatedAt()) {
            //No need to calculate all, Only Twap calculation is required, so wee need to add statistics
            //Tricky way to decrease the statistics calculation in such a case :
            //Tick count is not changed and there is no new tick for the instrument(updatedAt)
            instrumentTick.setCalculationType(CalculationType.TWAP);
        }
        else
            instrumentTick.setCalculationType(CalculationType.ALL);

        rabbitTemplate.convertAndSend(exchangeName, routingName, instrumentTick);
    }
}
