package com.itesm.panoptimize.controller;
import com.itesm.panoptimize.dto.note.NoteDTO;
import com.itesm.panoptimize.dto.supervisor.SupervisorCreateDTO;
import com.itesm.panoptimize.dto.supervisor.SupervisorUpdateDTO;
import com.itesm.panoptimize.dto.supervisor.SupervisorUserDTO;
import com.itesm.panoptimize.service.UserService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.security.Principal;

@RestController
@RequestMapping ("/supervisor")
public class SupervisorController {
    private final UserService supervisorService;
    private final UserService userService;

    public SupervisorController(UserService supervisorService, UserService userService) {
        this.supervisorService = supervisorService;
        this.userService = userService;
    }

    @Operation(summary = "Get all supervisors", description = "Get all supervisors from the system." )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Supervisors found.",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = SupervisorUserDTO.class))
                    }),
            @ApiResponse(responseCode = "404",
                    description = "Supervisors not found.",
                    content = @Content),
    })
    @GetMapping("/")
    public ResponseEntity<Page<SupervisorUserDTO>> getAllSupervisors(Pageable pageable) {
        return ResponseEntity.ok(supervisorService.getAllSupervisors(pageable));
    }

    @Operation(summary = "Create a new supervisor", description = "Create a new supervisor in the system" )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Supervisor created.",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = SupervisorUserDTO.class))
                    }),
            @ApiResponse(responseCode = "404",
                    description = "Supervisor not created.",
                    content = @Content),
    })
    @PostMapping("/")
    public ResponseEntity<SupervisorUserDTO> createSupervisor(@RequestBody SupervisorCreateDTO supervisorUserDTO) {
        return ResponseEntity.ok(supervisorService.createSupervisor(supervisorUserDTO));
    }

    @Operation(summary = "Obtener info de supervisor", description = "Obtener la info de supervisor mediante el id" )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Supervisor encontrado.",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = SupervisorUserDTO.class))
                    }),
            @ApiResponse(responseCode = "404",
                    description = "Supervisor not found.",
                    content = @Content),
    })
    @GetMapping("/{id}")
    public ResponseEntity<SupervisorUserDTO> getSupervisorById(@PathVariable Integer id) {
        return ResponseEntity.ok(supervisorService.getSupervisor(id));
    }

    @Operation(summary = "Get a supervisor", description = "Get a supervisor by its id." )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Supervisor Found.",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = SupervisorUserDTO.class))
                    }),
            @ApiResponse(responseCode = "404",
                    description = "Supervisor not found.",
                    content = @Content),
    })
    @GetMapping("/connect/{id}")
    public ResponseEntity<SupervisorUserDTO> getSupervisorByConnectId(@PathVariable String id) {
        SupervisorUserDTO supervisor = userService.getSupervisorWithConnectId(id);

        if (supervisor == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(supervisor);
    }

    @Operation(summary = "Delete supervisor", description = "Delete supervisor by its id" )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Supervisor deleted.",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = String.class))
                    }),
            @ApiResponse(responseCode = "400",
                    description = "Supervisor not deleted.",
                    content = @Content),
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSupervisor(@PathVariable Integer id) {
        supervisorService.deleteSupervisor(id);
        return ResponseEntity.ok("Supervisor deleted");
    }

    @Operation(summary = "Update an existing Supervisor",
            description = "This PUT request call updates an existing Supervisor.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Supervisor updated successfully.",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = SupervisorUserDTO.class))
                    }),
            @ApiResponse(responseCode = "404",
                    description = "Supervisor not found.",
                    content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<SupervisorUserDTO> updateSupervisor(@PathVariable Integer id, @RequestBody SupervisorUpdateDTO supervisorUserDTO) {
        return ResponseEntity.ok(supervisorService.updateSupervisor(id, supervisorUserDTO));
    }

    @Operation(summary = "Get agent info", description = "Get extra information about the agent by its id" )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Supervisor encontrado.",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = SupervisorUserDTO.class))
                    }),
            @ApiResponse(responseCode = "404",
                    description = "Supervisor no encontrado.",
                    content = @Content),
    })
    @GetMapping("/info")
    public ResponseEntity<SupervisorUserDTO> getSupervisorInfo(Principal principal) {
        String firebaseId = principal.getName();
        return ResponseEntity.ok(userService.getSupervisorWithFirebaseId(firebaseId));
    }
}