package com.rerecir.statisticsserver.repository;

import com.rerecir.statisticsserver.entity.Statistics;

public interface StatisticsRepository {

    Statistics getAggregated();

    Statistics get(String instrument);

    void insert(Statistics statistics);

}
