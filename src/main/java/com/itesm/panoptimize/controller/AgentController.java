package com.itesm.panoptimize.controller;

import com.itesm.panoptimize.dto.agent.*;
import com.itesm.panoptimize.dto.dashboard.DashboardFiltersDTO;
import com.itesm.panoptimize.dto.agent.AgentResponseDTO;
import com.itesm.panoptimize.model.*;
import com.itesm.panoptimize.service.AgentListService;
import com.itesm.panoptimize.service.FeedbackService;
import com.itesm.panoptimize.service.UserService;
import com.itesm.panoptimize.service.AgentListService;
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
import reactor.core.publisher.Mono;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


@RestController
@RequestMapping("/agent")
public class AgentController {

    private final UserService userService;
    private final FeedbackService feedbackService;

    private final AgentListService agentListService;

    @Autowired
    public AgentController(UserService userService, ModelMapper modelMapper, FeedbackService feedbackService, AgentListService agentListService, AgentListService agentsService) {
        this.userService = userService;
        this.feedbackService = feedbackService;
        this.agentListService = agentListService;
        this.agentsService = agentsService;
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
    public ResponseEntity<Page<AgentUserDTO>> getAllAgents(Pageable pageable) {
        return ResponseEntity.ok(userService.getallAgents(pageable));
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

    @GetMapping("/{id}")
    public ResponseEntity<AgentUserDTO> getAgentById(@PathVariable Integer id) {
        return ResponseEntity.ok(userService.getAgent(id));
    }

    @GetMapping("/connect/{id}")
    public ResponseEntity<AgentUserDTO> getAgentByConnectId(@PathVariable String id) {
        return ResponseEntity.ok(userService.getAgentWithConnectId(id));
    }

    @PostMapping("/")
    public ResponseEntity<AgentUserDTO> createAgent(@RequestBody AgentCreateDTO agentUserDTO) {
        return ResponseEntity.ok(userService.createAgent(agentUserDTO));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAgent(@PathVariable Integer id) {
        userService.deleteAgent(id);
        return ResponseEntity.ok("Agent deleted");
    }

    @PutMapping("/{id}")
    public ResponseEntity<AgentUserDTO> updateAgent(@PathVariable Integer id, @RequestBody AgentUpdateDTO agentUserDTO) {
        return ResponseEntity.ok(userService.updateAgent(id, agentUserDTO));
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
    @PutMapping("/agent/performance/update/{id}")
    public ResponseEntity<AgentPerformance> updateNotification(@PathVariable Integer id, @RequestBody AgentPerformance agentPerformance) {
        return ResponseEntity.ok(userService.updateAgentPerformance(id, agentPerformance));
    }
    @GetMapping("/agent/feedback/{id}")
    public ResponseEntity<Note> getFeedback(@PathVariable("id") Integer id){
        Note note = feedbackService.getFeedbackById(id);
        return new ResponseEntity<>(note, HttpStatus.OK);
    }
    @PostMapping("/agent/feedback/new")
    public ResponseEntity<String> addFeedback(){
        Note note = new Note();
        feedbackService.addFeedback(note);
        return new ResponseEntity<>("Feedback added", HttpStatus.OK);
    }
    @DeleteMapping("/agent/feedback/delete/{id}")
    public ResponseEntity<String> deleteFeedback(@PathVariable("id") Integer id){
        feedbackService.deleteFeedback(id);
        return new ResponseEntity<>("Feedback deleted", HttpStatus.OK);
    }
    @PutMapping("/agent/feedback/update/{id}")
    public ResponseEntity<Note> updateFeedback(@PathVariable Integer id, @RequestBody Note note) {
        return ResponseEntity.ok(feedbackService.updateFeedback(id, note));
    }

    /*
    * This enpoint gives a response directly from connect, without processing.*/
    @GetMapping("/list/{instanceId}")
    public ResponseEntity<DashboardFiltersDTO> getFilters(@PathVariable String instanceId) {
        DashboardFiltersDTO filters = agentListService.getAgentList(instanceId);

        System.out.println(instanceId);

        return ResponseEntity.ok(filters);
    }

    @GetMapping("/detail/{instanceId}/{agentId}")
    public ResponseEntity<AgentDetailsDTO> getAgentDetails(@PathVariable String agentId,@PathVariable String instanceId) {
        System.out.println(agentId);
        System.out.println(instanceId);
        AgentDetailsDTO agent = agentListService.getAgentDetails(agentId, instanceId);

        System.out.println(agentId);

        return ResponseEntity.ok(agent);
    }

    private final AgentListService agentsService;


    @PostMapping("/agentslist")
    public Mono<AgentResponseDTO> getAllAgents(@RequestParam String instanceId) {
        return agentsService.getAllAgents(instanceId)
                .map(AgentResponseDTO::new);
    }

}
