package com.rerecir.statisticsserver.repository.impl;

import com.rerecir.statisticsserver.entity.Statistics;
import com.rerecir.statisticsserver.repository.StatisticsRepository;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class StatisticsRepositoryImpl implements StatisticsRepository {

    //in-memory data storage of instrument-statistics
    private Map<String, Statistics> statisticsMap = new ConcurrentHashMap<>();

    @Override
    public Statistics getAggregated() {
        return statisticsMap.get("*");
    }

    @Override
    public Statistics get(String instrument) {
        return statisticsMap.get(instrument);
    }

    @Override
    //@RabbitListener(queues = "${rabbitmq.statistics.queue.name}")
    public void insert(Statistics statistics) {
        statisticsMap.put(statistics.getInstrument(), statistics);
    }
}
