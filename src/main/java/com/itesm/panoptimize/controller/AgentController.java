package com.itesm.panoptimize.controller;

import com.itesm.panoptimize.dto.agent.AgentDTO;
import com.itesm.panoptimize.dto.agent.StatusDTO;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;


import java.util.List;
import java.util.Map;


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

    @GetMapping("/agents/all")
    public ResponseEntity<List<AgentDTO>> getAllAgents(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        List<User> agentsPage = userService.getAllAgents(page, size);

        List<AgentDTO> agentsDTO = new ArrayList<>();
        for (User agent : agentsPage) {
            agentsDTO.add(convertToDTO(agent));
        }

        return ResponseEntity.ok(agentsDTO);
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
    //No pongo lo de Open Api por que me marca que no encuentra la dependencia
    //Mapping del dominio que nos dieron en la junta del 2 abril, hay que ver si se puede con el :
    @GetMapping("/agent/{id}")
    public ResponseEntity<AgentDTO> getAgent(@PathVariable("id") int id) {

        User agent = userService.getAgent(id);

        if (agent == null) {
            return ResponseEntity.notFound().build();
        }

        AgentDTO agentDTO = convertToDTO(agent);


        return  ResponseEntity.ok(agentDTO);
    }

    @PostMapping("/newAgent")
    public ResponseEntity<AgentDTO> createNewAgent(@RequestBody AgentDTO agentDTO) {
        
        return ResponseEntity.ok(agentDTO);
    }
    
    @DeleteMapping("/deleteAgent/{id}")
    public ResponseEntity<String> deleteAgentResponse(@PathVariable Long id){
        return ResponseEntity.ok("Agent " + id + " was deleted.");
    } 

    @PostMapping("/agent/feedback")
    public ResponseEntity<String> postFeedback(@RequestBody PostFeedbackDTO feedbackDTO) {
        return ResponseEntity.ok("Feedback enviado exitosamente \nFecha: " + feedbackDTO.getDate());
    }

}