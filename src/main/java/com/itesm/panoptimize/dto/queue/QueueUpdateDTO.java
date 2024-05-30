package com.itesm.panoptimize.dto.queue;

public class QueueUpdateDTO {
    private String name;

    public QueueUpdateDTO(String name) {
        this.name = name;
    }

    public String getName() {return name;}

    public void setName(String name) {this.name = name;}
}
