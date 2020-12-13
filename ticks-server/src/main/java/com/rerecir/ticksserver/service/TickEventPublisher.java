package com.rerecir.ticksserver.service;

import com.rerecir.ticksserver.enums.CalculationSource;

public interface TickEventPublisher {

    void publish(String instrument, CalculationSource calculationSource);
}
