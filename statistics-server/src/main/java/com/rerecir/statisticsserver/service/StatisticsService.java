package com.rerecir.statisticsserver.service;

import com.rerecir.statisticsserver.entity.dto.StatisticsDto;

public interface StatisticsService {

    StatisticsDto getStatistics();

    StatisticsDto getStatistics(String instrument);
}
