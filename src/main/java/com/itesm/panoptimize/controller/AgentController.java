package com.itesm.panoptimize.controller;

import com.itesm.panoptimize.dto.agent.AgentDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.ArrayList;


import java.util.List;

@RestController
public class AgentController {
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

}
