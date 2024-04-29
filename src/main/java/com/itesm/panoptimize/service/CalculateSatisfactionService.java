package com.itesm.panoptimize.service;
import com.itesm.panoptimize.dto.dashboard.DashboardSatisfactionDTO;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CalculateSatisfactionService {

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

    public String calculateSatisfaction(DashboardSatisfactionDTO satisfactionDTO){
        int speedOfAnswer = satisfactionDTO.getSpeedOfAnswer();
        int handleTime = satisfactionDTO.getHandleTime();
        boolean abandoned = satisfactionDTO.getAbandoned();

        int satisfaction = 0;

        if(!abandoned) 
        {
            satisfaction += calcAnswerTimeQuality(speedOfAnswer);
            satisfaction += calcHandleTimeQuality(handleTime);
            satisfaction /=20; //to switch case 0-4
        }

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
