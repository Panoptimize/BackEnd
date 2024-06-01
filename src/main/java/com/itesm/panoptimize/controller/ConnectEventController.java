package com.itesm.panoptimize.controller;

import com.itesm.panoptimize.dto.contact.ContactDTO;
import com.itesm.panoptimize.dto.contactevent.ContactEventDTO;
import com.itesm.panoptimize.service.ConnectEventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/connect")
public class ConnectEventController {

    private final ConnectEventService connectEventService;

    public ConnectEventController(ConnectEventService connectEventService) {
        this.connectEventService = connectEventService;
    }

    @Operation(summary = "Handle Amazon Connect event")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Event received")
    })
    @PostMapping("/event")
    public ResponseEntity<ContactDTO> handleConnectEvent(@RequestBody ContactEventDTO event) {
        ContactDTO contactDTO = connectEventService.handleConnectEvent(event);
        if (contactDTO != null) {
            return new ResponseEntity<>(contactDTO, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
