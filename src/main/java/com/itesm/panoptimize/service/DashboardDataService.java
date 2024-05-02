package com.itesm.panoptimize.service;
import org.springframework.stereotype.Service;

import org.json.JSONException;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.json.JSONObject;
@Service
public class DashboardDataService {

    private final RestTemplate restTemplate;
    public DashboardDataService() {
        this.restTemplate = new RestTemplate();
    }
    private Double getMetricValue(String metricUrl) throws JSONException {
        ResponseEntity<String> response = restTemplate.getForEntity(metricUrl, String.class);
        JSONObject jsonObject = new JSONObject(response.getBody());
        return jsonObject.getDouble("value");
    }

    public Double getAbandonRate() throws JSONException {
        String url = "http://127.0.0.1:8000/metrics/v2/ABANDONMENT_RATE";
        return getMetricValue(url);
    }

    public Double getAgentScheduleAdherence() throws JSONException {
        String url = "http://127.0.0.1:8000/metrics/v2/AGENT_SCHEDULE_ADHERENCE";
        return getMetricValue(url);
    }

    public Double getSumHandleTime() throws JSONException {
        String url = "http://127.0.0.1:8000/metrics/v2/SUM_HANDLE_TIME";
        return getMetricValue(url);
    }

    public Double getContactsHandled() throws JSONException {
        String url = "http://127.0.0.1:8000/metrics/v2/CONTACTS_HANDLED";
        return getMetricValue(url);
    }
}