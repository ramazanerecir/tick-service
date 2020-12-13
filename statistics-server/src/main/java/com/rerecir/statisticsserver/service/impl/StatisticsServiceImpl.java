package com.rerecir.statisticsserver.service.impl;

import com.rerecir.statisticsserver.entity.Statistics;
import com.rerecir.statisticsserver.entity.dto.StatisticsDto;
import com.rerecir.statisticsserver.repository.StatisticsRepository;
import com.rerecir.statisticsserver.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StatisticsServiceImpl implements StatisticsService {

    private final StatisticsRepository statisticsRepository;
    //TODO private final TickValidator tickValidator;

    @Override
    public StatisticsDto getStatistics() {
        Statistics statistics = statisticsRepository.getAggregated();

        return generateDto(statistics);
    }

    @Override
    public StatisticsDto getStatistics(String instrument) {
        Statistics statistics = statisticsRepository.get(instrument);

        return generateDto(statistics);
    }

    private StatisticsDto generateDto(Statistics statistics) {
        StatisticsDto dto = new StatisticsDto();

        //if instrument's last tick arrival is out of sliding time interval, statistics should be empty.
        if (statistics != null
         //TODO       && tickValidator.validateTimestamp(statistics.getInstrumentUpdatedAt())
        ) {
            dto.setAvg(statistics.getAvg());
            dto.setCount(statistics.getCount());
            dto.setMax(statistics.getMax());
            dto.setMaxDrawdown(statistics.getMaxDrawdown());
            dto.setMin(statistics.getMin());
            dto.setQuantile(statistics.getQuantile());
            dto.setTwap(statistics.getTwap());
            dto.setTwap2(statistics.getTwap2());
            dto.setVolatility(statistics.getVolatility());
        }

        return dto;
    }
}
