package com.itesm.panoptimize.controller;
import com.itesm.panoptimize.dto.agent.DashboardAgentDTO;
import com.itesm.panoptimize.service.DashboardAgentsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
@RestController
// Controller class for managing dashboard agents
public class AgentsController {

    private DashboardAgentsService agentService = new DashboardAgentsService();

    // Constructor for DashboardAgentsController
    public AgentsController(DashboardAgentsService DashboardAgentDTO) {
        this.agentService = agentService;
    }

    // Retrieves the status counts for the real-time dashboard
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
