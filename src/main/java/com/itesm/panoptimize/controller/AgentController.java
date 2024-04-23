package com.itesm.panoptimize.controller;

import com.itesm.panoptimize.dto.agent.AgentDTO;
import com.itesm.panoptimize.dto.agent.DashboardAgentDTO;
import com.itesm.panoptimize.dto.agent.PostFeedbackDTO;
import com.itesm.panoptimize.service.AgentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;


import java.util.List;
import java.util.Map;

@RestController
public class AgentController {

    private AgentService agentService = new AgentService();

    @Autowired
    public AgentController(AgentService agentService) {
        this.agentService = agentService;
    }

    @GetMapping("/agents/all")
    public ResponseEntity<List<AgentDTO>> getAllAgents(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        List<AgentDTO> allAgents= new ArrayList<>();

        AgentDTO agent1 = new AgentDTO("Dave Lombardo", "Sales", "Call", (int) (Math.random() * 101));
        allAgents.add(agent1);

        AgentDTO agent2 = new AgentDTO("Trevor Strnad", "Sales", "Call", (int) (Math.random() * 101));
        allAgents.add(agent2);

        AgentDTO agent3 = new AgentDTO("Mikael Akerfeldt", "Sales", "Call", (int) (Math.random() * 101));
        allAgents.add(agent3);

        AgentDTO agent4 = new AgentDTO("George Fisher", "Sales", "Call", (int) (Math.random() * 101));
        allAgents.add(agent4);

        AgentDTO agent5 = new AgentDTO("Angela Gossow", "Sales", "Call", (int) (Math.random() * 101));
        allAgents.add(agent5);

        AgentDTO agent6 = new AgentDTO("Phil Bozeman", "Sales", "Call", (int) (Math.random() * 101));
        allAgents.add(agent6);

        int startIndex = page * size;
        int endIndex = Math.min(startIndex + size, allAgents.size());
        List<AgentDTO> agentsPage = allAgents.subList(startIndex, endIndex);

        return ResponseEntity.ok(agentsPage);

    }

    //Documentación de Open API
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



    @GetMapping("/agent/:id")
    public ResponseEntity<AgentDTO> getAgent(){
        //List<workspaceDTO> workspaces = new ArrayList<>();
        AgentDTO agentDTO = new AgentDTO();
        agentDTO.setId(1); //Ver si es necesario cambiar esto, seria solamente para la comparación con BD
        agentDTO.setName("Dave Parker");

        return  ResponseEntity.ok(agentDTO);
    }

    @PostMapping("/agent/feedback")
    public ResponseEntity<String> postFeedback(@RequestBody PostFeedbackDTO feedbackDTO) {
        return ResponseEntity.ok("Feedback enviado exitosamente \nFecha: " + feedbackDTO.getDate());
    }

    @GetMapping("/dashboard/agents")
    @Operation(summary = "Obtain status for real-time dashboard")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Agents status recollected.",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = DashboardAgentDTO.class))
                    }),
            @ApiResponse(responseCode = "404",
                    description = "Status Not found.",
                    content = @Content)
    })
    public Map<String, Integer> getAgentStatusCountsForDashboard() {
        // Generate sample agents (you can replace this with actual data from your service)
        List<DashboardAgentDTO> agents = generateSampleAgents();

        // Calculate status counts using the agent service
        Map<String, Integer> statusCounts = agentService.calculateStatusCounts(agents);

        return statusCounts;
    }

    // Generates sample agents for testing purposes
    private List<DashboardAgentDTO> generateSampleAgents() {
        List<DashboardAgentDTO> agents = new ArrayList<>();
        agents.add(new DashboardAgentDTO("Agent 1", DashboardAgentDTO.AgentStatus.AVAILABLE));
        agents.add(new DashboardAgentDTO("Agent 2", DashboardAgentDTO.AgentStatus.IN_CONTACT));
        agents.add(new DashboardAgentDTO("Agent 3", DashboardAgentDTO.AgentStatus.AFTER_CALL_WORK));
        agents.add(new DashboardAgentDTO("Agent 4", DashboardAgentDTO.AgentStatus.OFFLINE));
        agents.add(new DashboardAgentDTO("Agent 5", DashboardAgentDTO.AgentStatus.AVAILABLE));
        return agents;
    }
}