package com.itesm.panoptimize.dto.dashboard;

import jakarta.validation.constraints.NotBlank;
public class CallMetricsDTO {
    private int speedOfAnswer;
    private int handleTime;
    private boolean abandoned;

    public int getSpeedOfAnswer() {
        return speedOfAnswer;
    }

    public void setSpeedOfAnswer(int speedOfAnswer) {
        this.speedOfAnswer = speedOfAnswer;
    }

    public int getHandleTime() {
        return handleTime;
    }

    public void setHandleTime(int handleTime) {
        this.handleTime = handleTime;
    }

    public boolean isAbandoned() { return abandoned; }

    public void setAbandoned(boolean abandoned) { this.abandoned = abandoned; }
}
