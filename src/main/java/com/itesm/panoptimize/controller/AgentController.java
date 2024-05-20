package com.itesm.panoptimize.controller;

import com.itesm.panoptimize.dto.agent.AgentDTO;
import com.itesm.panoptimize.dto.agent.PostFeedbackDTO;
import com.itesm.panoptimize.model.User;
import com.itesm.panoptimize.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;


import java.util.List;


@RestController
public class AgentController {

    private final UserService userService;
    private final ModelMapper modelMapper;

    @Autowired
    public AgentController(UserService userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    private AgentDTO convertToDTO(User user) {
        return modelMapper.map(user, AgentDTO.class);
    }

    @Operation(summary = "Obtener todos los agentes", description = "Obtener todos los agentes registrados en el sistema" )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Agentes encontrados.",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = AgentDTO.class))
                    }),
            @ApiResponse(responseCode = "404",
                    description = "Agentes no encontrados.",
                    content = @Content),
    })
    @GetMapping("/agents/all")
    public ResponseEntity<Page<AgentDTO>> getAllAgents(Pageable pageable) {

        Page<AgentDTO> agentsPage = userService.getAllAgents(pageable);
        return ResponseEntity.ok(agentsPage);
    }

    @Operation(summary = "Obtener info  de agente", description = "Obtener la info de agente mediante el id" )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Agente encontrado.",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = AgentDTO.class))
                    }),
            @ApiResponse(responseCode = "404",
                    description = "Agente no encontrado.",
                    content = @Content),
    })

    @GetMapping("/agent/{id}")
    public ResponseEntity<AgentDTO> getAgent(@PathVariable("id") int id) {

        User agent = userService.getUser(id);

        if (agent == null) {
            return ResponseEntity.notFound().build();
        }

        AgentDTO agentDTO = convertToDTO(agent);


        return  ResponseEntity.ok(agentDTO);
    }

    @PostMapping("/")
    public ResponseEntity<AgentDTO> createNewAgent(@RequestBody AgentDTO agentDTO) {
        
        return ResponseEntity.ok(agentDTO);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAgentResponse(@PathVariable Long id){
        return ResponseEntity.ok("Agent " + id + " was deleted.");
    } 

    @PostMapping("/agent/feedback")
    public ResponseEntity<String> postFeedback(@RequestBody PostFeedbackDTO feedbackDTO) {
        return ResponseEntity.ok("Feedback enviado exitosamente \nFecha: " + feedbackDTO.getDate());
    }

}