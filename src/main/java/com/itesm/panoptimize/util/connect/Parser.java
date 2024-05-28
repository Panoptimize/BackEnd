package com.itesm.panoptimize.util.connect;

import com.itesm.panoptimize.dto.connect.*;
import com.itesm.panoptimize.dto.connect.MetricInterval;
import com.itesm.panoptimize.dto.connect.Threshold;
import com.itesm.panoptimize.dto.contact.MetricResultDTO;
import software.amazon.awssdk.services.connect.model.*;

import java.util.ArrayList;
import java.util.List;

public class Parser {
    public static GetMetricResponseDTO serializeMetricResponse(GetMetricDataV2Response metricDataV2Response) {
        List<MetricResultDTO> metricResults = new ArrayList<>();
        GetMetricResponseDTO getMetricResponseDTO = new GetMetricResponseDTO();

        List<MetricResult> metricResultsDTO = new ArrayList<>();
        for (MetricResultV2 metricData : metricDataV2Response.metricResults()) {
            MetricInterval metricInterval = new MetricInterval();
            MetricResult metricResult = new MetricResult();
            List<Collection> collections = new ArrayList<>();

            for (MetricDataV2 metric : metricData.collections()) {
                Collection collection = new Collection();
                Metric metricDTO = new Metric();

                List<Threshold> thresholds = new ArrayList<>();
                for(ThresholdV2 threshold : metric.metric().threshold()) {
                    Threshold thresholdDTO = new Threshold();
                    thresholdDTO.setThresholdValue(threshold.thresholdValue());
                    thresholdDTO.setComparison(thresholdDTO.getComparison());
                    thresholds.add(thresholdDTO);
                }
                List<MetricFilter> metricFilters = new ArrayList<>();
                for(MetricFilterV2 metricFilter : metric.metric().metricFilters()) {
                    MetricFilter metricFilterDTO = new MetricFilter();
                    metricFilterDTO.setMetricFilterKey(metricFilter.metricFilterKey());
                    metricFilterDTO.setMetricFilterValues(metricFilter.metricFilterValues());
                    metricFilters.add(metricFilterDTO);
                }

                metricDTO.setThreshold(thresholds);
                metricDTO.setName(metric.metric().name());
                metricDTO.setMetricFilters(metricFilters);

                collection.setMetric(metricDTO);
                collection.setValue(metric.value());
                collections.add(collection);
            }

            metricInterval.setInterval(metricData.metricInterval().interval());
            metricInterval.setStartTime(metricData.metricInterval().startTime());

            metricResult.setDimensions(metricData.dimensions());
            metricResult.setMetricInterval(metricInterval);
            metricResult.setCollections(collections);

            metricResultsDTO.add(metricResult);
        }

        getMetricResponseDTO.setMetricResults(metricResultsDTO);
        getMetricResponseDTO.setNextToken(metricDataV2Response.nextToken());

        return getMetricResponseDTO;
    }
}
