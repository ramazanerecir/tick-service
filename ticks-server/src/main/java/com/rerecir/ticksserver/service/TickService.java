package com.rerecir.ticksserver.service;

import com.rerecir.ticksserver.entity.dto.TickDto;

public interface TickService {

    boolean insertTick(TickDto tickDto);

    void sendToTickQueue(TickDto tickDto);
}
