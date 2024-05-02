package com.itesm.panoptimize.service;
import com.itesm.panoptimize.dto.dashboard.CallMetricsDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;

@Service
public class CalculateSatisfactionService {

    private final WebClient.Builder webClientBuilder;

    @Autowired
    public CalculateSatisfactionService(WebClient.Builder webClientBuilder){

        this.webClientBuilder = webClientBuilder;
    }

    public List<CallMetricsDTO> getCallMetrics() {
        return webClientBuilder.build()
                .get()
                .uri("http://127.0.0.1:8000")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<CallMetricsDTO>>() {})
                .block();
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
        List<Integer> results = new ArrayList<>(List.of(0,0,0,0,0));
        for(CallMetricsDTO metrics: call_metrics) {
            System.out.println(metrics.getSpeedOfAnswer());
            int satisfactionLevel = calculateSatisfaction(metrics);
            System.out.println(satisfactionLevel);
            results.set(satisfactionLevel, results.get(satisfactionLevel) + 1);
        }
        return results;
    }

    public int calculateSatisfaction(CallMetricsDTO metrics){
        int speedOfAnswer = metrics.getSpeedOfAnswer();
        int handleTime = metrics.getHandleTime();
        boolean abandoned = metrics.isAbandoned();

        int satisfaction = 0;

        if(!abandoned)
        {
            satisfaction += calcAnswerTimeQuality(speedOfAnswer);
            satisfaction += calcHandleTimeQuality(handleTime);
        }
        if(satisfaction < 20) { return 0; }
        else if (satisfaction < 40) { return 1; }
        else if (satisfaction < 60) { return 2; }
        else if (satisfaction < 80) { return 3; }
        else { return 4; }
    }
}