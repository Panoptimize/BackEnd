package com.itesm.panoptimize.service;
import com.itesm.panoptimize.dto.dashboard.DashboardSatisfactionDTO;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CalculateSatisfactionService {

    public String calculateSatisfaction(DashboardSatisfactionDTO satisfactionDTO){
        int speedOfAnswer = satisfactionDTO.getSpeedOfAnswer();
        int handleTime = satisfactionDTO.getHandleTime();
        int afterCallworkTime = satisfactionDTO.getAfterCallworkTime();
        boolean abandoned = satisfactionDTO.getAbandoned();

        int satisfaction = 0;

        if(speedOfAnswer < 30) { satisfaction++; }
        if(handleTime < 300) { satisfaction++; }
        if(afterCallworkTime < 300) { satisfaction++; }
        if(!abandoned) { satisfaction++; }

        switch(satisfaction){
            case 0:
                return "Very unsatisfied";
            case 1:
                return "Unsatisfied";
            case 2:
                return "Neutral";
            case 3:
                return "Satisfied";
            case 4:
                return "Very satisfied";
            default:
                return "";
        }
    }
}