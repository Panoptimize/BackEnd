package com.itesm.panoptimize.controller;

import com.itesm.panoptimize.dto.agent.AgentDTO;
import com.itesm.panoptimize.dto.agent.PostFeedbackDTO;
import com.itesm.panoptimize.model.*;
import com.itesm.panoptimize.service.FeedbackService;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;


import java.util.List;


@RestController
@RequestMapping("/agent")
public class AgentController {

    private final UserService userService;
    private final ModelMapper modelMapper;
    private final FeedbackService feedbackService;

    @Autowired
    public AgentController(UserService userService, ModelMapper modelMapper, FeedbackService feedbackService) {
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.feedbackService = feedbackService;
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
    @GetMapping("/")
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
    @GetMapping("/agent/performance/{id}")
    public ResponseEntity<AgentPerformance> getAgentPerformance(@PathVariable("id") Integer id){
        AgentPerformance agentPerformance = userService.getAgentPerformance(id);
        return new ResponseEntity<>(agentPerformance, HttpStatus.OK);
    }
    @PostMapping("/agent/performance/new")
    public ResponseEntity<String> addAgentPerformance(){
        AgentPerformance agentPerformance = new AgentPerformance();
        userService.addAgentPerformance(agentPerformance);
        return new ResponseEntity<>("Agent performance added", HttpStatus.OK);
    }
    @DeleteMapping("/agent/performance/delete/{id}")
    public ResponseEntity<String> deleteAgentPerformance(@PathVariable("id") Integer id){
        userService.deleteAgentPerformance(id);
        return new ResponseEntity<>("Agent performance deleted", HttpStatus.OK);
    }
    @PatchMapping("/agent/performance/update/{id}")
    public ResponseEntity<AgentPerformance> updateNotification(@PathVariable Integer id, @RequestBody AgentPerformance agentPerformance) {
        return ResponseEntity.ok(userService.updateAgentPerformance(id, agentPerformance));
    }
    @GetMapping("/agent/feedback/{id}")
    public ResponseEntity<Feedback> getFeedback(@PathVariable("id") Integer id){
        Feedback feedback = feedbackService.getFeedbackById(id);
        return new ResponseEntity<>(feedback, HttpStatus.OK);
    }
    @PostMapping("/agent/feedback/new")
    public ResponseEntity<String> addFeedback(){
        Feedback feedback = new Feedback();
        feedbackService.addFeedback(feedback);
        return new ResponseEntity<>("Feedback added", HttpStatus.OK);
    }
    @DeleteMapping("/agent/feedback/delete/{id}")
    public ResponseEntity<String> deleteFeedback(@PathVariable("id") Integer id){
        feedbackService.deleteFeedback(id);
        return new ResponseEntity<>("Feedback deleted", HttpStatus.OK);
    }
    @PatchMapping("/agent/feedback/update/{id}")
    public ResponseEntity<Feedback> updateFeedback(@PathVariable Integer id, @RequestBody Feedback feedback) {
        return ResponseEntity.ok(feedbackService.updateFeedback(id, feedback));
    }

}