package com.rerecir.statisticsserver.api.impl;

import com.rerecir.statisticsserver.api.StatisticsApi;
import com.rerecir.statisticsserver.entity.dto.StatisticsDto;
import com.rerecir.statisticsserver.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("statistics")
@RequiredArgsConstructor
public class StatisticsApiImpl implements StatisticsApi {

    private final StatisticsService statisticsService;

    @Override
    @GetMapping
    public ResponseEntity<StatisticsDto> statistics() {
        return ResponseEntity.ok(statisticsService.getStatistics());
    }

    @Override
    @GetMapping("/{instrument}")
    public ResponseEntity<StatisticsDto> statistics(@PathVariable("instrument") String instrument) {
        return ResponseEntity.ok(statisticsService.getStatistics(instrument));
    }
}
