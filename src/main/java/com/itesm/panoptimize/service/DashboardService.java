package com.itesm.panoptimize.service;

import com.itesm.panoptimize.config.Constants;
import com.itesm.panoptimize.dto.dashboard.DashboardDTO;
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
    private double[] callKPIs(String metricRequest) {
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
        String requestJson = """
                {
                  "end_time": 168000,
                  "filters": [
                    {
                      "filter_key": "region",
                      "filter_values": ["us-west-1", "us-east-1"]
                    }
                  ],
                  "groupings": ["service"],
                  "interval": {
                    "interval_period": "1h",
                    "time_zone": "UTC"
                  },
                  "max_results": 100,
                  "metrics": [
                    {
                      "metric_filters": [
                        {
                          "metric_filter_key": "status",
                          "metric_filter_values": ["success"],
                          "negate": false
                        }
                      ],
                      "name": "AGENT_SCHEDULE_ADHERENCE",
                      "threshold": [
                        {
                          "comparison": "greater_than",
                          "threshold_value": 90
                        }
                      ]
                    },
                    {
                      "metric_filters": [],
                      "name": "ABANDONMENT_RATE",
                      "threshold": [
                        {
                          "comparison": "less_than",
                          "threshold_value": 5
                        }
                      ]
                    },
                    {
                      "metric_filters": [],
                      "name": "CONTACTS_HANDLED",
                      "threshold": [
                        {
                          "comparison": "greater_than",
                          "threshold_value": 1000
                        }
                      ]
                    },
                    {
                      "metric_filters": [],
                      "name": "SUM_HANDLE_TIME",
                      "threshold": [
                        {
                          "comparison": "greater_than",
                          "threshold_value": 15000
                        }
                      ]
                    },
                    {
                      "metric_filters": [],
                      "name": "SERVICE_LEVEL",
                      "threshold": [
                        {
                          "comparison": "greater_than",
                          "threshold_value": 80
                        }
                      ]
                    },
                    {
                      "metric_filters": [],
                      "name": "AVG_HOLD_TIME",
                      "threshold": [
                        {
                          "comparison": "less_than",
                          "threshold_value": 120
                        }
                      ]
                    },
                    {
                      "metric_filters": [],
                      "name": "OCCUPANCY",
                      "threshold": [
                        {
                          "comparison": "greater_than",
                          "threshold_value": 75
                        }
                      ]
                    }
                  ],
                  "next_token": "",
                  "resource_arn": "arn:aws:lambda:us-west-1:123456789012:function:my-lambda-function",
                  "start_time": 167000
                }""";

        return webClient.post()
                .uri("/metrics/data")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestJson)
                .retrieve()
                .bodyToMono(MetricsDTO.class)
                .block();
    }
}