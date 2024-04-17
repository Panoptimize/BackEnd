package com.example.demo.controller;

import com.example.demo.dto.AgentDTO;
import com.example.demo.dto.PostCrateDTO;
import com.example.demo.dto.PostDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;


import java.util.ArrayList;
import java.util.List;

@RestController
public class AgentController {
    @Operation(summary = "Get list of agents", description = "Get list of agents of a certain size and in pages")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                        description = "Found agents",
                        content = {
                            @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PostDTO.class))
                        }),
            @ApiResponse(responseCode = "404",
                    description = "Agents not found",
                    content = @Content),
            })

    @GetMapping("/agents/all")
    public ResponseEntity<List<AgentDTO>> getAllAgents(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        List<AgentDTO> allAgents= new ArrayList<>();

        AgentDTO agent1 = createAgent("Dave Lombardo", "Sales", (int) (Math.random() * 101), "Call");
        allAgents.add(agent1);

        AgentDTO agent2 = createAgent("Trevor Strnad", "Sales", (int) (Math.random() * 101), "Call");
        allAgents.add(agent2);

        AgentDTO agent3 = createAgent("Mikael Akerfeldt", "Sales", (int) (Math.random() * 101), "Call");
        allAgents.add(agent3);

        AgentDTO agent4 = createAgent("George Fisher", "Sales", (int) (Math.random() * 101), "Call");
        allAgents.add(agent4);

        AgentDTO agent5 = createAgent("Angela Gossow", "Sales", (int) (Math.random() * 101), "Call");
        allAgents.add(agent5);

        AgentDTO agent6 = createAgent("Phil Bozeman", "Sales", (int) (Math.random() * 101), "Call");
        allAgents.add(agent6);





        int startIndex = page * size;
        int endIndex = Math.min(startIndex + size, allAgents.size());
        List<AgentDTO> agentsPage = allAgents.subList(startIndex, endIndex);

        return ResponseEntity.ok(agentsPage);

    }

    private AgentDTO createAgent(String name, String workspace, int temperature, String currentContactM) {
        AgentDTO agent = new AgentDTO();
        agent.setName(name);
        agent.setWorkspace(workspace);
        agent.setIntTemperature(temperature);
        agent.setCurrentContactM(currentContactM);
        return agent;
    }


    @PostMapping("/post")
    public ResponseEntity<PostCrateDTO> createPost(PostCrateDTO post) {
        return ResponseEntity.ok(post);
    }
}
