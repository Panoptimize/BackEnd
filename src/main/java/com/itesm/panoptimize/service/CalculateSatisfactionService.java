package com.itesm.panoptimize.service;
import com.itesm.panoptimize.dto.dashboard.CallMetricsDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class CalculateSatisfactionService {

    private final RestTemplate restTemplate;

    @Autowired
    public CalculateSatisfactionService(RestTemplate restTemplate){
        this.restTemplate = restTemplate;
    }

    public List<CallMetricsDTO> getCallMetrics() {
        String apiUrl = "http://127.0.0.1:8000/";

        CallMetricsDTO[] call_metrics = restTemplate.getForObject(apiUrl, CallMetricsDTO[].class);

        return List.of(call_metrics);
    }

    public static int calcAnswerTimeQuality(int answer_time){
        int quality=30;//we assign 30 max points for answer time and 70 to handle time
        int leniency=20;//time we give the agent (in seconds) to answer without any penalty    
        int rhythm=2;//how often (in seconds) a point is taken off of quality
    
        int penalty=(answer_time-leniency)/rhythm;//points to be taken off
        quality-= penalty;
        if (quality<0){
          quality=0;
        }
        return quality;
      }
    
    public static int calcHandleTimeQuality(int handle_time){
        int quality=70;//we assign 30 max points for answer time and 70 to handle time
        int leniency=90;//time we give the agent (in seconds) to handle without any penalty, maybe better use AHT
        int rhythm=6;//how often (in seconds) a point is taken off of quality
    
        int penalty=(handle_time-leniency)/rhythm;//points to be taken off
        quality-= penalty;
        if (quality<0){
          quality=0;
        }
        return quality;
      }

      public List<Integer> calculateSatisfaction(List<CallMetricsDTO> call_metrics){
        List<Integer> results = new ArrayList<>(List.of(0,0,0,0));
        for(CallMetricsDTO metrics: call_metrics) {
            int satisfactionLevel = calculateSatisfaction(metrics);
            results.set(satisfactionLevel, results.get(satisfactionLevel) + 1);
        }
        return results;
      }

    public int calculateSatisfaction(CallMetricsDTO metrics){
        int speedOfAnswer = metrics.getSpeedOfAnswer();
        int handleTime = metrics.getHandleTime();
        boolean abandoned = metrics.getAbandoned();

        int satisfaction = 0;

        if(!abandoned) 
        {
            satisfaction += calcAnswerTimeQuality(speedOfAnswer);
            satisfaction += calcHandleTimeQuality(handleTime);
            satisfaction /=20; //to switch case 0-4
        }
        if(satisfaction < 20) { return 0; }
        else if (satisfaction < 40) { return 1; }
        else if (satisfaction < 60) { return 2; }
        else if (satisfaction < 80) { return 3; }
        else { return 4; }
    }
}
