package com.itesm.panoptimize.dto.dashboard;

import jakarta.validation.constraints.NotBlank;

/*
POST
/dashboard/data
{
    timeframe: string,
    agent: int []
    workspace: int []
}
*/
public class DashboardSatisfactionDTO {
    @NotBlank
    private int instance_id;

    private int speed_of_answer;
    private int handle_time;
    private int after_callwork_time;
    private boolean abandoned;

    public int getInstance_id() { return instance_id; }

    public void setInstance_id(int instance_id) { this.instance_id = instance_id; }
    public int getSpeedOfAnswer() {
        return speed_of_answer;
    }

    public void setSpeedOfAnswer(int speed_of_answer) {
        this.speed_of_answer = speed_of_answer;
    }

    public int getHandleTime() {
        return handle_time;
    }

    public void setHandleTime(int handle_time) {
        this.handle_time = handle_time;
    }

    public int getAfterCallworkTime() {
        return after_callwork_time;
    }

    public void setAfterCallworkTime(int after_callwork_time) {
        this.after_callwork_time = after_callwork_time;
    }

    public boolean getAbandoned() { return abandoned; }

    public void setAbandoned(boolean abandoned) { this.abandoned = abandoned; }
}
