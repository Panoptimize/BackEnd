package com.itesm.panoptimize.service;

import com.itesm.panoptimize.config.Constants;
import com.itesm.panoptimize.dto.dashboard.DashboardDTO;
import com.itesm.panoptimize.dto.metric.MetricRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import com.itesm.panoptimize.dto.dashboard.MetricsDTO;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

@Service
public class DashboardService {
    private final WebClient webClient;

    @Autowired
    public DashboardService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:8000").build();
    }
    private double[] callKPIs(MetricRequest metricRequest) {
        WebClient.UriSpec<WebClient.RequestBodySpec> uriSpec = webClient.post();
        WebClient.RequestBodySpec bodySpec = uriSpec.uri("/get_metric_data");
        WebClient.RequestHeadersSpec<?> headersSpec = bodySpec.bodyValue(metricRequest);
        WebClient.ResponseSpec responseSpec = headersSpec
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .acceptCharset(StandardCharsets.UTF_8)
                .retrieve();
        Mono<ResponseEntity<double[]>> responseEntityMono = responseSpec.toEntity(double[].class);
        ResponseEntity<double[]> responseEntity = responseEntityMono.block();
        assert responseEntity != null;
        return responseEntity.getBody();
    }

    public List<Double> getKPIs(DashboardDTO dashboardDTO) throws ParseException {
        String[] dates = dashboardDTO.getTimeframe().split(Constants.DAT_INTERVAL_DELIMITER);
        Date startDate = DateFormat.getDateInstance().parse(dates[0].trim());
        Date endDate = DateFormat.getDateInstance().parse(dates[1].trim());

        // Convert dates to integers
        int start = (int) startDate.getTime();
        int end = (int) endDate.getTime();



        //Dummy method
        return List.of(1.0, 2.0, 3.0);
    }
    public MetricsDTO getMetricsData() {
        String requestJson = "{\n" +
                "  \"end_time\": 168000,\n" +
                "  \"filters\": [\n" +
                "    {\n" +
                "      \"filter_key\": \"region\",\n" +
                "      \"filter_values\": [\"us-west-1\", \"us-east-1\"]\n" +
                "    }\n" +
                "  ],\n" +
                "  \"groupings\": [\"service\"],\n" +
                "  \"interval\": {\n" +
                "    \"interval_period\": \"1h\",\n" +
                "    \"time_zone\": \"UTC\"\n" +
                "  },\n" +
                "  \"max_results\": 100,\n" +
                "  \"metrics\": [\n" +
                "    {\n" +
                "      \"metric_filters\": [\n" +
                "        {\n" +
                "          \"metric_filter_key\": \"status\",\n" +
                "          \"metric_filter_values\": [\"success\"],\n" +
                "          \"negate\": false\n" +
                "        }\n" +
                "      ],\n" +
                "      \"name\": \"AGENT_SCHEDULE_ADHERENCE\",\n" +
                "      \"threshold\": [\n" +
                "        {\n" +
                "          \"comparison\": \"greater_than\",\n" +
                "          \"threshold_value\": 90\n" +
                "        }\n" +
                "      ]\n" +
                "    },\n" +
                "    {\n" +
                "      \"metric_filters\": [],\n" +
                "      \"name\": \"ABANDONMENT_RATE\",\n" +
                "      \"threshold\": [\n" +
                "        {\n" +
                "          \"comparison\": \"less_than\",\n" +
                "          \"threshold_value\": 5\n" +
                "        }\n" +
                "      ]\n" +
                "    },\n" +
                "    {\n" +
                "      \"metric_filters\": [],\n" +
                "      \"name\": \"CONTACTS_HANDLED\",\n" +
                "      \"threshold\": [\n" +
                "        {\n" +
                "          \"comparison\": \"greater_than\",\n" +
                "          \"threshold_value\": 1000\n" +
                "        }\n" +
                "      ]\n" +
                "    },\n" +
                "    {\n" +
                "      \"metric_filters\": [],\n" +
                "      \"name\": \"SUM_HANDLE_TIME\",\n" +
                "      \"threshold\": [\n" +
                "        {\n" +
                "          \"comparison\": \"greater_than\",\n" +
                "          \"threshold_value\": 15000\n" +
                "        }\n" +
                "      ]\n" +
                "    },\n" +
                "    {\n" +
                "      \"metric_filters\": [],\n" +
                "      \"name\": \"SERVICE_LEVEL\",\n" +
                "      \"threshold\": [\n" +
                "        {\n" +
                "          \"comparison\": \"greater_than\",\n" +
                "          \"threshold_value\": 80\n" +
                "        }\n" +
                "      ]\n" +
                "    },\n" +
                "    {\n" +
                "      \"metric_filters\": [],\n" +
                "      \"name\": \"AVG_HOLD_TIME\",\n" +
                "      \"threshold\": [\n" +
                "        {\n" +
                "          \"comparison\": \"less_than\",\n" +
                "          \"threshold_value\": 120\n" +
                "        }\n" +
                "      ]\n" +
                "    },\n" +
                "    {\n" +
                "      \"metric_filters\": [],\n" +
                "      \"name\": \"OCCUPANCY\",\n" +
                "      \"threshold\": [\n" +
                "        {\n" +
                "          \"comparison\": \"greater_than\",\n" +
                "          \"threshold_value\": 75\n" +
                "        }\n" +
                "      ]\n" +
                "    }\n" +
                "  ],\n" +
                "  \"next_token\": \"\",\n" +
                "  \"resource_arn\": \"arn:aws:lambda:us-west-1:123456789012:function:my-lambda-function\",\n" +
                "  \"start_time\": 167000\n" +
                "}";

        MetricsDTO metricdata = webClient.post()
                .uri("/metrics/data")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestJson)
                .retrieve()
                .bodyToMono(MetricsDTO.class)
                .block();

        return metricdata;


    }

}