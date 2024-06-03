package com.itesm.panoptimize.controller;

import com.itesm.panoptimize.dto.agent_performance.AgentPerformanceDTO;
import com.itesm.panoptimize.dto.note.NoteDTO;
import com.itesm.panoptimize.service.AgentPerformanceService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/agent-performance")
public class AgentPerformance {
    private final AgentPerformanceService agentPerformanceService;
    public AgentPerformance(AgentPerformanceService agentPerformanceService) {this.agentPerformanceService = agentPerformanceService;}

    @GetMapping("/")
    public ResponseEntity<Page<AgentPerformanceDTO>> getNotes(Pageable pageable){
        return ResponseEntity.ok(agentPerformanceService.getAgentPerformances(pageable));
    }
}