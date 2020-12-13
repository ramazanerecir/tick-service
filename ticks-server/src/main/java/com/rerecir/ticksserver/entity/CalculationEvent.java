package com.rerecir.ticksserver.entity;

import com.rerecir.ticksserver.enums.CalculationSource;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CalculationEvent implements Serializable {

    String instrument;
    CalculationSource calculationSource;
}
