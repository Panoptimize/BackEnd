package com.itesm.panoptimize.controller;


import com.itesm.panoptimize.dto.agent.StatusDTO;
import com.itesm.panoptimize.service.StatusService;
import com.itesm.panoptimize.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController()
@RequestMapping("/status")
public class StatusController {

    private final StatusService statusService;
    private final UserService userService;

    @Autowired
    public StatusController(StatusService statusService, UserService userService) {
        this.statusService = statusService;
        this.userService = userService;
    }

    @GetMapping("/")
    public List<StatusDTO> getStatus(Principal principal) {
        String firebaseId = principal.getName();
        String instanceId = userService.getInstanceIdFromFirebaseId(firebaseId);
        return statusService.getActiveAgents(instanceId);
    }
}
