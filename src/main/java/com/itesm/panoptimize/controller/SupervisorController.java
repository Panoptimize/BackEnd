package com.itesm.panoptimize.controller;
import com.itesm.panoptimize.dto.agent.AgentDTO;
import com.itesm.panoptimize.dto.supervisor.SupervisorDTO;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping ("/supervisors")
public class SupervisorController {
    @Operation(
            summary = "Get all the supervisors",
            description = "Get the supervisor name and details without passwords or sensitive data"
    )
    @ApiResponses(value={
            @ApiResponse(responseCode = "200",
                    description = "Supervisors found",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = SupervisorDTO.class))
                    }),
            @ApiResponse(responseCode = "404",
                    description = "Supervisors not found",
                    content = @Content),
    })
    @GetMapping("/supervisors/all")
    public ResponseEntity<List<SupervisorDTO>> getController(){
        List<SupervisorDTO> posts = new ArrayList<>();
        SupervisorDTO supervisorDto= new SupervisorDTO ();
        supervisorDto.setName("Caligula de Argona");
        supervisorDto.setFloor("Sales_Floor");
        supervisorDto.setVerified(true);
        supervisorDto.setPicture("https://example.com/Caligula.jpg");
        SupervisorDTO superDto = new SupervisorDTO();
        superDto.setName("Steven Smith");
        superDto.setFloor("CostumerSupport_Floor");
        superDto.setVerified(true);
        superDto.setPicture("https://example.com/StevenSmith.jpg");


        posts.add(supervisorDto);
        posts.add(superDto);

        return ResponseEntity.ok(posts);
    }

    @Operation(summary = "Deleting a supervisor", description = "Director deletes a supervisor.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Supervisor deleted",
                    content = @Content),
            @ApiResponse(responseCode = "404",
                    description = "Unable to delete",
                    content = @Content)
    })
    @DeleteMapping("/supervisor/{id}")
    public ResponseEntity<String> deleteSupervisorResponse(@PathVariable Long id){
        return ResponseEntity.ok("Supervisor " + id + " was deleted.");
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update a supervisor", description = "Updates the details of an existing supervisor by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Supervisor updated successfully",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = SupervisorDTO.class))
                    }),
            @ApiResponse(responseCode = "404",
                    description = "Supervisor not found",
                    content = @Content)
    })
    public ResponseEntity<SupervisorDTO> updateSupervisor(@PathVariable int id, @RequestBody SupervisorDTO supervisorDetails) {
        SupervisorDTO initialSupervisor = new SupervisorDTO();
        createSupervisor(initialSupervisor);

        return ResponseEntity.ok(initialSupervisor);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SupervisorDTO> getSupervisorById(@PathVariable int id) {
        List<SupervisorDTO> supervisors = new ArrayList<>();
        SupervisorDTO initialSupervisor = new SupervisorDTO();
        createSupervisor(initialSupervisor);
        List<AgentDTO> supervisedAgents = new ArrayList<>();
        AgentDTO agent1 = new AgentDTO("Dave Lombardo", "Sales", "Call", (int) (Math.random() * 101));
        AgentDTO agent2 = new AgentDTO("Trevor Strnad", "Sales", "Call", (int) (Math.random() * 101));
        supervisedAgents.add(agent1);
        supervisedAgents.add(agent2);

        initialSupervisor.setSupervisedAgents(supervisedAgents);

        supervisors.add(initialSupervisor);
        //API to get a supervisor by ID
        for (SupervisorDTO supervisor : supervisors) {
            if (supervisor.getId() == id) {
                return ResponseEntity.ok(supervisor);
            }
        }
        return ResponseEntity.notFound().build();
    }


    @Operation(
            summary = "Get all the agents from the supervisor",
            description = "Get the id's of all the agents under the supervisors command"
    )
    @ApiResponses(value={
            @ApiResponse(responseCode = "200",
                    description = "Agents found",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = SupervisorDTO.class))
                    }),
            @ApiResponse(responseCode = "404",
                    description = "Agents not found",
                    content = @Content),
    })
    @GetMapping("/{id}/agents")
    public ResponseEntity<List<AgentDTO>> getAgentsUnderSupervision(@PathVariable int id){
        
        SupervisorDTO supervisorDto0 = new SupervisorDTO ();
        supervisorDto0.setId(0);
        SupervisorDTO supervisorDto1 = new SupervisorDTO ();
        supervisorDto1.setId(1);
        SupervisorDTO supervisorDto2 = new SupervisorDTO ();
        supervisorDto2.setId(2);

        List<AgentDTO> agentList0 = new ArrayList<>();
        List<AgentDTO> agentList1 = new ArrayList<>();
        List<AgentDTO> agentList2 = new ArrayList<>();

        AgentDTO agent1 = new AgentDTO("Dave Lombardo", "Sales", "Call", (int) (Math.random() * 101));
        agentList0.add(agent1);
        AgentDTO agent2 = new AgentDTO("Trevor Strnad", "Sales", "Call", (int) (Math.random() * 101));
        agentList0.add(agent2);
        AgentDTO agent3 = new AgentDTO("Mikael Akerfeldt", "Sales", "Call", (int) (Math.random() * 101));
        agentList1.add(agent3);
        AgentDTO agent4 = new AgentDTO("George Fisher", "Sales", "Call", (int) (Math.random() * 101));
        agentList2.add(agent4);
        AgentDTO agent5 = new AgentDTO("Angela Gossow", "Sales", "Call", (int) (Math.random() * 101));
        agentList2.add(agent5);
        AgentDTO agent6 = new AgentDTO("Phil Bozeman", "Sales", "Call", (int) (Math.random() * 101));
        agentList2.add(agent6);

        supervisorDto0.setSupervisedAgents(agentList0);
        supervisorDto1.setSupervisedAgents(agentList1);
        supervisorDto2.setSupervisedAgents(agentList2);

        List<SupervisorDTO> supervisors = new ArrayList<>();
        supervisors.add(supervisorDto0);
        supervisors.add(supervisorDto1);
        supervisors.add(supervisorDto2);

        for (SupervisorDTO supervisor : supervisors) {
                if (supervisor.getId() == id) {
                    return ResponseEntity.ok(supervisor.getSupervisedAgents());
                }
            }

        return ResponseEntity.notFound().build();

        }



    // Temporal method, to not repeat code
    private void createSupervisor(SupervisorDTO initialSupervisor) {
        initialSupervisor.setId(1);
        initialSupervisor.setName("Supervisor Uno");
        initialSupervisor.setEmail("supervisor1@empresa.com");
        initialSupervisor.setUsername("supervisorUno");
        initialSupervisor.setPassword("123456");
        initialSupervisor.setFloor("Main Floor");
        initialSupervisor.setVerified(true);
        initialSupervisor.setPicture("https://example.com/supervisorUno.jpg");
    }
}