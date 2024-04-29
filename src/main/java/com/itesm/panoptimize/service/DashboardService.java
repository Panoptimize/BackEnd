package com.itesm.panoptimize.service;

import com.itesm.panoptimize.config.Constants;
import com.itesm.panoptimize.dto.dashboard.DashboardDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

@Service
public class DashboardService {


    private final RestTemplate restTemplate;

    @Autowired
    public DashboardService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private List<Double> callKPIs(DashboardDTO dashboardDTO) {
        String url = "http://localhost:8080/dashboard/data/download";
        ResponseEntity<List<Double>> responseEntity = restTemplate.exchange(url, HttpMethod.POST, null, new ParameterizedTypeReference<>() {
        });
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
