package com.itesm.panoptimize.controller;
import com.itesm.panoptimize.dto.SupervisorDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping ("/supervisors")
public class SupervisorController {
    private final List<SupervisorDTO> supervisors = new ArrayList<>();
    public SupervisorController() {
        SupervisorDTO initialSupervisor = new SupervisorDTO();
        initialSupervisor.setId(1);
        initialSupervisor.setName("Supervisor Uno");
        initialSupervisor.setEmail("supervisor1@empresa.com");
        initialSupervisor.setUsername("supervisorUno");
        initialSupervisor.setPassword("123456");
        List<String> supervisedAgents = new ArrayList<>();
        supervisedAgents.add("Agent1");
        supervisedAgents.add("Agent2");
        initialSupervisor.setSupervisedAgents(supervisedAgents);
        supervisors.add(initialSupervisor);
    }


    @PatchMapping("/{id}")
    public ResponseEntity<SupervisorDTO> updateSupervisor(@PathVariable int id, @RequestBody SupervisorDTO supervisorDetails) {
        for (SupervisorDTO supervisor : supervisors) {
            if (supervisor != null){
                if (supervisor.getId() == id) {
                    if (supervisorDetails.getName() != null) supervisor.setName(supervisorDetails.getName());
                    if (supervisorDetails.getEmail() != null) supervisor.setEmail(supervisorDetails.getEmail());
                    if (supervisorDetails.getUsername() != null) supervisor.setUsername(supervisorDetails.getUsername());
                    if (supervisorDetails.getPassword() != null) supervisor.setPassword(supervisorDetails.getPassword());
                    if (supervisorDetails.getSupervisedAgents() != null) supervisor.setSupervisedAgents(supervisorDetails.getSupervisedAgents());
                    return ResponseEntity.ok(supervisor);
                }
            }

        }
        return ResponseEntity.notFound().build();
    }
}