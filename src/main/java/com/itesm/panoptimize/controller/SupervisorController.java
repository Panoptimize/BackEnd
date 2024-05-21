package com.itesm.panoptimize.controller;
import com.itesm.panoptimize.dto.agent.AgentDTO;
import com.itesm.panoptimize.dto.supervisor.SupervisorCreateDTO;
import com.itesm.panoptimize.dto.supervisor.SupervisorDTO;
import com.itesm.panoptimize.dto.supervisor.SupervisorUpdateDTO;
import com.itesm.panoptimize.dto.supervisor.SupervisorUserDTO;
import com.itesm.panoptimize.service.UserService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping ("/supervisor")
public class SupervisorController {
    private final UserService supervisorService;

    public SupervisorController(UserService supervisorService) {
        this.supervisorService = supervisorService;
    }

    @GetMapping("/")
    public ResponseEntity<Page<SupervisorUserDTO>> getAllSupervisors(Pageable pageable) {
        return ResponseEntity.ok(supervisorService.getAllSupervisors(pageable));
    }

    @PostMapping("/")
    public ResponseEntity<SupervisorUserDTO> createSupervisor(@RequestBody SupervisorCreateDTO supervisorUserDTO) {
        return ResponseEntity.ok(supervisorService.createSupervisor(supervisorUserDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SupervisorUserDTO> getSupervisorById(@PathVariable Integer id) {
        return ResponseEntity.ok(supervisorService.getSupervisor(id));
    }

    @GetMapping("/connect/{id}")
    public ResponseEntity<SupervisorUserDTO> getSupervisorByConnectId(@PathVariable String id) {
        return ResponseEntity.ok(supervisorService.getSupervisorWithConnectId(id));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSupervisor(@PathVariable Integer id) {
        supervisorService.deleteSupervisor(id);
        return ResponseEntity.ok("Supervisor deleted");
    }

    @PutMapping("/{id}")
    public ResponseEntity<SupervisorUserDTO> updateSupervisor(@PathVariable Integer id, @RequestBody SupervisorUpdateDTO supervisorUserDTO) {
        return ResponseEntity.ok(supervisorService.updateSupervisor(id, supervisorUserDTO));
    }
}