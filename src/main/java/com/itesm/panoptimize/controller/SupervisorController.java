package com.itesm.panoptimize.controller;
import com.itesm.panoptimize.dto.agent.AgentDTO;
import com.itesm.panoptimize.dto.supervisor.SupervisorCreateDTO;
import com.itesm.panoptimize.dto.supervisor.SupervisorDTO;
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
}