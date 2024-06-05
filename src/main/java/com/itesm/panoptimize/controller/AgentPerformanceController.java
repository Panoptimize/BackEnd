package com.itesm.panoptimize.controller;

import com.itesm.panoptimize.dto.agent_performance.AgentPerformanceDTO;
import com.itesm.panoptimize.dto.agent_performance.AgentPerformanceMetricsDTO;
import com.itesm.panoptimize.dto.agent_performance.CreateAgentPerformanceDTO;
import com.itesm.panoptimize.service.AgentPerformanceService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/agent-performance")
public class AgentPerformanceController {
    private final AgentPerformanceService agentPerformanceService;
    public AgentPerformanceController(AgentPerformanceService agentPerformanceService) {this.agentPerformanceService = agentPerformanceService;}

    @GetMapping("/")
    public ResponseEntity<Page<AgentPerformanceDTO>> getAgentPerformances(Pageable pageable){
        return ResponseEntity.ok(agentPerformanceService.getAgentPerformances(pageable));
    }

    @GetMapping("/note/{id}")
    public ResponseEntity<AgentPerformanceDTO> getAgentPerformanceByNote(@PathVariable Integer id){
        return ResponseEntity.ok(agentPerformanceService.getAgentPerformanceByNote(id));
    }

    @GetMapping("/details/{id}")
    public ResponseEntity<AgentPerformanceMetricsDTO> getAgentMetricsToday(@PathVariable Integer id){
        return ResponseEntity.ok(agentPerformanceService.getAgentMetricsToday(id));
    }

    @PostMapping("/")
    public ResponseEntity<AgentPerformanceDTO> createAgentPerformance(@RequestBody CreateAgentPerformanceDTO createAgentPerformanceDTO){
        return ResponseEntity.ok(agentPerformanceService.createAgentPerformance(createAgentPerformanceDTO));
    }
}
