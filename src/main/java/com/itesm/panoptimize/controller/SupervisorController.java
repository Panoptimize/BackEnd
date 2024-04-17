package com.itesm.panoptimize.controller;

import com.itesm.panoptimize.dto.supervisor.SupervisorDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/supervisors")
public class SupervisorController {
    private final List<SupervisorDTO> supervisors = new ArrayList<>();

    public SupervisorController() {
        // Initialization of a supervisor example
        SupervisorDTO initialSupervisor = new SupervisorDTO();
        initialSupervisor.setId(1);
        initialSupervisor.setName("Supervisor Uno");
        initialSupervisor.setEmail("supervisor1@empresa.com");
        initialSupervisor.setUsername("supervisorUno");
        initialSupervisor.setPassword("123456");
        initialSupervisor.setFloor("Main Floor");
        initialSupervisor.setVerified(true);
        initialSupervisor.setPicture("https://example.com/supervisorUno.jpg");
        List<String> supervisedAgents = new ArrayList<>();
        supervisedAgents.add("Agent1");
        supervisedAgents.add("Agent2");
        initialSupervisor.setSupervisedAgents(supervisedAgents);

        supervisors.add(initialSupervisor);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SupervisorDTO> getSupervisorById(@PathVariable int id) {
        //API to get a supervisor by ID
        for (SupervisorDTO supervisor : supervisors) {
            if (supervisor.getId() == id) {
                return ResponseEntity.ok(supervisor);
            }
        }
        return ResponseEntity.notFound().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<SupervisorDTO> updateSupervisor(@PathVariable int id, @RequestBody SupervisorDTO supervisorDetails) {
        //API to update the details of a supervisor identified by the provided ID
        for (SupervisorDTO supervisor : supervisors) {
            if (supervisor.getId() == id) {
                if (supervisorDetails.getName() != null) supervisor.setName(supervisorDetails.getName());
                if (supervisorDetails.getEmail() != null) supervisor.setEmail(supervisorDetails.getEmail());
                if (supervisorDetails.getUsername() != null) supervisor.setUsername(supervisorDetails.getUsername());
                if (supervisorDetails.getPassword() != null) supervisor.setPassword(supervisorDetails.getPassword());
                if (supervisorDetails.getFloor() != null) supervisor.setFloor(supervisorDetails.getFloor());
                if (supervisorDetails.getPicture() != null) supervisor.setPicture(supervisorDetails.getPicture());
                return ResponseEntity.ok(supervisor);
            }
        }
        return ResponseEntity.notFound().build();
    }
}
