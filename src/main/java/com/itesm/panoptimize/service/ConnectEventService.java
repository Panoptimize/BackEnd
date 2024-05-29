package com.itesm.panoptimize.service;

import org.springframework.stereotype.Service;

@Service
public class ConnectEventService {

    public void validateEvent(Object event) {
        if (event == null) {
            throw new IllegalArgumentException("Event cannot be null");
        }
    }
}
