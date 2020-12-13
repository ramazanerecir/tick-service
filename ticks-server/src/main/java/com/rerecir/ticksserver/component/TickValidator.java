package com.rerecir.ticksserver.component;

import com.rerecir.ticksserver.entity.dto.TickDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
public class TickValidator {

    public boolean validateTick(TickDto tickDto) {
        return (!(tickDto.getInstrument() == null ||
                tickDto.getInstrument().isEmpty() ||
                Double.isNaN(tickDto.getPrice()) ||
                Double.isInfinite(tickDto.getPrice()) ||
                tickDto.getPrice() <= 0.0));
    }
}
