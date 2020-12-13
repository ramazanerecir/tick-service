package com.rerecir.ticksserver.entity;


import com.rerecir.common.enums.CalculationType;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class InstrumentTick implements Serializable {

    String instrument;
    List<Tick> tickList;

    long updatedAt;

    //Transient
    CalculationType calculationType;

    public InstrumentTick(String instrument) {
        this.instrument = instrument;
        this.tickList = new ArrayList<>();
    }
}
