package com.itesm.panoptimize.service;

import com.itesm.panoptimize.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.connect.ConnectClient;
import software.amazon.awssdk.services.connect.model.*;

import java.time.Instant;
import java.util.*;


@Service
public class CalculatePerformanceService {

    private final ConnectClient connectClient;

    @Autowired
    public CalculatePerformanceService(ConnectClient connectClient) {
        this.connectClient = connectClient;
    }
    private GetMetricDataV2Response getKPIs(List<MetricV2> metrics, String instanceId, Instant startTime, Instant endTime) {

        return connectClient.getMetricDataV2(GetMetricDataV2Request.builder()
                .startTime(startTime)
                .endTime(endTime)
                .groupings("AGENT")
                .resourceArn(Constants.BASE_ARN + ":instance/" + instanceId)
                .metrics(metrics)
                .build());
    }

    private List<MetricV2> setupMetrics() {
        List<MetricV2> metricList = new ArrayList<>();

        MetricV2 total_talk_time = MetricV2.builder()
                .name("TOTAL_TALK_TIME")
                .threshold(ThresholdV2.builder()
                        .comparison("LT")
                        .thresholdValue(80.0)
                        .build())
                .build();

        MetricV2 total_hold_time = MetricV2.builder()
                .name("TOTAL_HOLD_TIME")
                .threshold(ThresholdV2.builder()
                        .comparison("LT")
                        .thresholdValue(80.0)
                        .build())
                .build();

        MetricV2 total_after_call_work_time = MetricV2.builder()
                .name("TOTAL_AFTER_CALL_WORK_TIME")
                .threshold(ThresholdV2.builder()
                        .comparison("LT")
                        .thresholdValue(80.0)
                        .build())
                .build();
        MetricV2 contacts_handled = MetricV2.builder()
                .name("CONTACTS_HANDLED")
                .threshold(ThresholdV2.builder()
                        .comparison("LT")
                        .thresholdValue(80.0)
                        .build())
                .build();

        return metricList;
    }


    public Map<String, List<Double>> calculateAHT(String instanceId, Instant startDate, Instant endDate) {
        List<MetricV2> metrics = setupMetrics();
        GetMetricDataV2Response response = getKPIs(metrics, instanceId, startDate, endDate);

        Map<String, List<Double>> agentMetrics = new HashMap<>();

        for (MetricResultV2 result : response.metricResults()) {
            String agentId = result.dimensions().get("AGENT_ID");
            agentMetrics.putIfAbsent(agentId, new ArrayList<>());

            double totalTalkTime = 0;
            double totalHoldTime = 0;
            double totalACWTime = 0;
            double contactsHandled = 0;

            for (MetricDataV2 data : result.collections()) {
                switch (data.metric().name()) {
                    case "TOTAL_TALK_TIME":
                        totalTalkTime += data.value();
                        break;
                    case "TOTAL_HOLD_TIME":
                        totalHoldTime += data.value();
                        break;
                    case "TOTAL_AFTER_CALL_WORK_TIME":
                        totalACWTime += data.value();
                        break;
                    case "CONTACTS_HANDLED":
                        contactsHandled += data.value();
                        break;
                }
            }

            double aht = contactsHandled > 0 ? (totalTalkTime + totalHoldTime + totalACWTime) / contactsHandled : 0;
            agentMetrics.get(agentId).add(aht);
        }

        return agentMetrics;
    }


}


