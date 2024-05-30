package com.itesm.panoptimize.scheduler;

import com.itesm.panoptimize.dto.performance.PerformanceDTO;
import com.itesm.panoptimize.service.StorePerformanceData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Component
public class ScheduledTasks {

    @Autowired
    private StorePerformanceData storePerformanceData;
    @Scheduled(cron = "0 0 0 * * ?")
    public void performTask() {
        LocalDate today = LocalDate.now();
        PerformanceDTO performanceDTO = new PerformanceDTO();
        performanceDTO.setStartDate(today);
        performanceDTO.setEndDate(today);
        performanceDTO.setInstanceId("7c78bd60-4a9f-40e5-b461-b7a0dfaad848");
        performanceDTO.setQueues(new String[]{});
        performanceDTO.setRoutingProfiles(new String[]{"4896ae34-a93e-41bc-8231-bf189e7628b1"});
        storePerformanceData.getMetricsData(performanceDTO);
    }
}
