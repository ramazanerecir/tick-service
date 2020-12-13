package com.rerecir.ticksserver.component;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TimestampValidator {

    @Value("${tick.sliding.time.interval}")
    private long timeInterval;

    public boolean validateTimestamp(long tickTimestamp) {
        return tickTimestamp >= (System.currentTimeMillis() - timeInterval);
    }
}
