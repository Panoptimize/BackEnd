package com.itesm.panoptimize.controller;

import com.itesm.panoptimize.service.CurrentUserDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/user-data")
public class UserDataController {
    private final CurrentUserDataService currentUserDataService;

    @Autowired
    public UserDataController(CurrentUserDataService currentUserDataService) {
        this.currentUserDataService = currentUserDataService;
    }

    @PostMapping("/userdata/{instanceId}")
    public ResponseEntity<Object> getCurrentUserData(@PathVariable String instanceId, @RequestBody Map<String, Object> requestPayload) {
        System.out.println("Received instanceId: " + instanceId);
        System.out.println("Received payload: " + requestPayload);
        try {
            Object userData = currentUserDataService.getCurrentUserData(instanceId, requestPayload);
            return ResponseEntity.ok(userData);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error retrieving user data: " + e.getMessage());
        }
    }
}