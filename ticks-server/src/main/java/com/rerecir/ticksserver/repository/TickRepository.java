package com.rerecir.ticksserver.repository;

import com.rerecir.ticksserver.entity.InstrumentTick;
import com.rerecir.ticksserver.entity.dto.TickDto;

import java.util.List;

public interface TickRepository {

    void insert(TickDto tickDto);

    List<String> getInstrumentList();

    InstrumentTick getFilteredInstrumentTick(String instrument);

    InstrumentTick getFilteredAllTicks();

}
