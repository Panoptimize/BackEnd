package com.itesm.panoptimize.controller;
import com.tec.panoptimize.dto.agent.DashboardAgentsDTO;
import com.tec.panoptimize.service.DashboardAgentsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
@RestController
// Controller class for managing dashboard agents
public class DashboardAgentsController {

    private final DashboardAgentsService agentService;

    // Constructor for DashboardAgentsController
    public DashboardAgentsController(DashboardAgentsService agentService) {
        this.agentService = agentService;
    }

    // Retrieves the status counts for the real-time dashboard
    @GetMapping("/dashboard/agents")
    public Map<String, Integer> getAgentStatusCountsForDashboard() {
        // Generate sample agents (you can replace this with actual data from your service)
        List<DashboardAgentsDTO> agents = generateSampleAgents();

        // Calculate status counts using the agent service
        Map<String, Integer> statusCounts = agentService.calculateStatusCounts(agents);

        return statusCounts;
    }

    // Generates sample agents for testing purposes
    private List<DashboardAgentsDTO> generateSampleAgents() {
        List<DashboardAgentsDTO> agents = new ArrayList<>();
        agents.add(new DashboardAgentsDTO("Agent 1", DashboardAgentsDTO.AgentStatus.AVAILABLE));
        agents.add(new DashboardAgentsDTO("Agent 2", DashboardAgentsDTO.AgentStatus.IN_CONTACT));
        agents.add(new DashboardAgentsDTO("Agent 3", DashboardAgentsDTO.AgentStatus.AFTER_CALL_WORK));
        agents.add(new DashboardAgentsDTO("Agent 4", DashboardAgentsDTO.AgentStatus.OFFLINE));
        agents.add(new DashboardAgentsDTO("Agent 5", DashboardAgentsDTO.AgentStatus.AVAILABLE));
        return agents;
    }
}