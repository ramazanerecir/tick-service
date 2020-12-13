package com.rerecir.ticksserver.repository.impl;

import com.rerecir.ticksserver.component.TimestampValidator;
import com.rerecir.ticksserver.entity.InstrumentTick;
import com.rerecir.ticksserver.entity.Tick;
import com.rerecir.ticksserver.entity.dto.TickDto;
import com.rerecir.ticksserver.enums.CalculationSource;
import com.rerecir.ticksserver.repository.TickRepository;
import com.rerecir.ticksserver.service.TickEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class TickRepositoryImpl implements TickRepository {

    private final TickEventPublisher tickEventPublisher;
    private final TimestampValidator timestampValidator;

    //in-memory data storage of instrument-ticks
    private Map<String, InstrumentTick> instrumentMap = new ConcurrentHashMap<>();

    /*
     * Listens Tick-Queue
     * Inserts Tick to memory
     * Creates tick events to trigger sending ticks to calculation queue for statistics calculation
     * */
    @Override
    @RabbitListener(queues = "${rabbitmq.tick.queue.name}")
    public void insert(TickDto tickDto) {
        insertInstrumentTick(tickDto.getInstrument(), new Tick(tickDto.getPrice(), tickDto.getTimestamp()));
        createTickEvent(tickDto.getInstrument());
    }

    private void insertInstrumentTick(String instrument, Tick tick) {
        instrumentMap.putIfAbsent(instrument, new InstrumentTick(instrument));
        instrumentMap.get(instrument).getTickList().add(tick);
        instrumentMap.get(instrument).setUpdatedAt(System.currentTimeMillis());
    }

    protected void createTickEvent(String instrument) {
        tickEventPublisher.publish(instrument, CalculationSource.NEWTICK);
        tickEventPublisher.publish("*", CalculationSource.NEWTICK);
    }

    @Override
    public InstrumentTick getFilteredInstrumentTick(String instrument) {
        InstrumentTick instrumentTick;
        if (!instrumentMap.containsKey(instrument)) {
            instrumentTick = new InstrumentTick(instrument);
        } else {
            instrumentTick = new InstrumentTick();
            instrumentTick.setInstrument(instrument);

            instrumentTick.setUpdatedAt(instrumentMap.get(instrument).getUpdatedAt());

            //no need to collect tick list in case last tick arrival time is out of sliding time interval
            if (timestampValidator.validateTimestamp(instrumentTick.getUpdatedAt())) {
                instrumentTick.setTickList(instrumentMap.get(instrument).getTickList()
                        .parallelStream()
                        .filter(t -> timestampValidator.validateTimestamp(t.getTimestamp()))
                        .collect(Collectors.toList()));
            } else {
                instrumentTick.setTickList(new ArrayList<>());
            }

        }
        return instrumentTick;
    }

    @Override
    public InstrumentTick getFilteredAllTicks() {
        InstrumentTick instrumentTick = new InstrumentTick();
        instrumentTick.setInstrument("*");

        instrumentTick.setUpdatedAt(instrumentMap.values()
                .parallelStream()
                .mapToLong(InstrumentTick::getUpdatedAt)
                .max().orElse(0));

        //no need to collect tick list in case last tick arrival time is out of sliding time interval
        if (timestampValidator.validateTimestamp(instrumentTick.getUpdatedAt())) {
            instrumentTick.setTickList(instrumentMap.values()
                    .parallelStream()
                    .map(InstrumentTick::getTickList)
                    .flatMap(List::stream)
                    .filter(t -> timestampValidator.validateTimestamp(t.getTimestamp()))
                    .collect(Collectors.toList()));
        } else {
            instrumentTick.setTickList(new ArrayList<>());
        }

        return instrumentTick;
    }

    @Override
    public List<String> getInstrumentList() {
        //Filtering instruments which is in sliding time interval
        return instrumentMap.values()
                .parallelStream()
                .filter(i -> timestampValidator.validateTimestamp(i.getUpdatedAt()))
                .map(InstrumentTick::getInstrument)
                .collect(Collectors.toList());
    }

}
