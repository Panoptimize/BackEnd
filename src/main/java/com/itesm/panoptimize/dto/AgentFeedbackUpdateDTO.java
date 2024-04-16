package com.itesm.panoptimize.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AgentFeedbackUpdateDTO {
    @NotBlank
    @Size(min = 0, max = 250)
    private String title;
    private String content;

    public String getTitle(){
        return title;
    }
    public void setTitle(String title){
        this.title = title;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
}
