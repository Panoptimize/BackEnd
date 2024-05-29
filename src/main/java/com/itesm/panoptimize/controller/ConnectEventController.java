package com.itesm.panoptimize.controller;

import com.itesm.panoptimize.dto.contact_event.ContactEvent;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/connect")
public class ConnectEventController {
    private static final Logger logger = LoggerFactory.getLogger(ConnectEventController.class);

    @Operation(summary = "Handle Amazon Connect event")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Event received")
    })
    @PostMapping("/event")
    public ResponseEntity<String> handleConnectEvent(@RequestBody ContactEvent event) {
        logger.info("Received Amazon Connect event: {}", event);

        // Process the event here

        return new ResponseEntity<>("Event received", HttpStatus.OK);
    }
}
