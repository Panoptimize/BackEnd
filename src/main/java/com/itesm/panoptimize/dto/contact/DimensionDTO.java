package com.itesm.panoptimize.dto.contact;
import com.fasterxml.jackson.annotation.JsonProperty;

public class DimensionDTO {
    @JsonProperty("Channel")
    private String channel;

    // Getters y Setters
    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }
}
