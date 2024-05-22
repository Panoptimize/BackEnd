package com.itesm.panoptimize.controller;
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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping ("/supervisor")
public class SupervisorController {
    private final UserService supervisorService;

    public SupervisorController(UserService supervisorService) {
        this.supervisorService = supervisorService;
    }

    @Operation(summary = "Obtener todos los supervisores", description = "Obtener todos los supervisores registrados en el sistema" )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Supervisores encontrados.",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = SupervisorDTO.class))
                    }),
            @ApiResponse(responseCode = "404",
                    description = "Supervisores no encontrados.",
                    content = @Content),
    })
    @GetMapping("/")
    public ResponseEntity<Page<SupervisorUserDTO>> getAllSupervisors(Pageable pageable) {
        return ResponseEntity.ok(supervisorService.getAllSupervisors(pageable));
    }

    @Operation(summary = "Crear un supervisor", description = "Crear un supervisor en el sistema" )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Supervisor creado.",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = SupervisorUserDTO.class))
                    }),
            @ApiResponse(responseCode = "404",
                    description = "Supervisor no creado.",
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
                    description = "Supervisor no encontrado.",
                    content = @Content),
    })
    @GetMapping("/{id}")
    public ResponseEntity<SupervisorUserDTO> getSupervisorById(@PathVariable Integer id) {
        return ResponseEntity.ok(supervisorService.getSupervisor(id));
    }

    @Operation(summary = "Obtener info de supervisor", description = "Obtener la info de supervisor mediante el id de connect" )
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
    @GetMapping("/connect/{id}")
    public ResponseEntity<SupervisorUserDTO> getSupervisorByConnectId(@PathVariable String id) {
        return ResponseEntity.ok(supervisorService.getSupervisorWithConnectId(id));
    }

    @Operation(summary = "Eliminar supervisor", description = "Eliminar supervisor mediante el id" )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Supervisor eliminado.",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = String.class))
                    }),
            @ApiResponse(responseCode = "400",
                    description = "Supervisor no eliminado.",
                    content = @Content),
    })
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