package com.itesm.panoptimize.controller;

import com.itesm.panoptimize.dto.agent_performance.AgentPerformanceDTO;
import com.itesm.panoptimize.dto.note.NoteDTO;
import com.itesm.panoptimize.service.AgentPerformanceService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@RestController
@RequestMapping("/agent-performance")
public class AgentPerformance {
    private final AgentPerformanceService agentPerformanceService;
    public AgentPerformance(AgentPerformanceService agentPerformanceService) {this.agentPerformanceService = agentPerformanceService;}

    @GetMapping("/")
    public ResponseEntity<Page<AgentPerformanceDTO>> getAgentPerformances(Pageable pageable){
        return ResponseEntity.ok(agentPerformanceService.getAgentPerformances(pageable));
    }

    @GetMapping("/note/{id}")
    public ResponseEntity<AgentPerformanceDTO> getAgentPerformanceByNote(@PathVariable Integer id){
        return ResponseEntity.ok(agentPerformanceService.getAgentPerformanceByNote(id));
    }

    @GetMapping("/details/{id}")
    public ResponseEntity<AgentPerformanceDTO> getAgentMetricsToday(@PathVariable Integer id){
        return ResponseEntity.ok(agentPerformanceService.getAgentMetricsToday(id));
    }
}
