package com.itesm.panoptimize.service;

import com.itesm.panoptimize.config.Constants;
import com.itesm.panoptimize.dto.dashboard.DashboardDTO;
import com.itesm.panoptimize.dto.metric.MetricRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

@Service
public class DashboardService {

    private final WebClient webClient;

    @Autowired
    public DashboardService(WebClient webClient) {
        this.webClient = webClient;
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
}
