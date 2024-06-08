package com.itesm.panoptimize.controller;

import com.itesm.panoptimize.dto.agent.*;
import com.itesm.panoptimize.dto.dashboard.DashboardFiltersDTO;
import com.itesm.panoptimize.dto.agent.AgentResponseDTO;
import com.itesm.panoptimize.dto.download.DownloadDTO;
import com.itesm.panoptimize.dto.note.NoteDTO;
import com.itesm.panoptimize.model.*;
import com.itesm.panoptimize.service.AgentListService;
import com.itesm.panoptimize.service.FeedbackService;
import com.itesm.panoptimize.service.UserService;
import com.itesm.panoptimize.service.AgentListService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


@RestController
@RequestMapping("/agent")
public class AgentController {

    private final UserService userService;
    private final FeedbackService feedbackService;

    private final AgentListService agentListService;

    @Autowired
    public AgentController(UserService userService, ModelMapper modelMapper, FeedbackService feedbackService, AgentListService agentListService, AgentListService agentsService) {
        this.userService = userService;
        this.feedbackService = feedbackService;
        this.agentListService = agentListService;
        this.agentsService = agentsService;
    }

    @Operation(summary = "Read agents", description = "This GET request call serves the purpose of returning all the Agents." )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Agents found.",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = AgentUserDTO.class))
                    }),
            @ApiResponse(responseCode = "404",
                    description = "Agents not found.",
                    content = @Content),
    })
    @GetMapping("/")
    public ResponseEntity<Page<AgentUserDTO>> getAllAgents(Pageable pageable) {
        return ResponseEntity.ok(userService.getallAgents(pageable));
    }


    /*GetIdAgent -- Fully Tested - Only finish Invalid Input*/
    @Operation(summary = "Get an agent", description = "This GET request call serves the purpose of returning an Agent by its id." )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Agent found.",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = AgentUserDTO.class))
                    }),
            @ApiResponse(responseCode = "404",
                    description = "Agent not found.",
                    content = @Content),
    })
    @GetMapping("/{id}")
    public ResponseEntity<AgentUserDTO> getAgentById(@PathVariable Integer id) {
        return ResponseEntity.ok(userService.getAgent(id));
    }

    /*GetIdAgentConnectId -- Fully Tested -- Finish Invalid*/
    @Operation(summary = "Get an agent", description = "This GET request call serves the purpose of returning an Agent by its connect id." )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Agent found.",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = AgentUserDTO.class))
                    }),
            @ApiResponse(responseCode = "404",
                    description = "Agent not found.",
                    content = @Content),
    })
    @GetMapping("/connect/{id}")
    public ResponseEntity<AgentUserDTO> getAgentByConnectId(@PathVariable String id) {
        return ResponseEntity.ok(userService.getAgentWithConnectId(id));
    }

    @Operation(summary = "Create a new agent",
            description = "This POST request call creates a new agent.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Agent created successfully."),
            @ApiResponse(responseCode = "400",
                    description = "Bad request.",
                    content = @Content)
    })
    @PostMapping("/")
    public ResponseEntity<AgentUserDTO> createAgent(@RequestBody AgentCreateDTO agentUserDTO) {
        return ResponseEntity.ok(userService.createAgent(agentUserDTO));
    }

    @Operation(summary = "Delete an agent by ID",
            description = "This DELETE request call deletes an agent by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204",
                    description = "Agent deleted successfully."),
            @ApiResponse(responseCode = "404",
                    description = "Agent not found.",
                    content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAgent(@PathVariable Integer id) {
        userService.deleteAgent(id);
        return ResponseEntity.ok("Agent deleted");
    }

    @Operation(summary = "Update an existing agent",
            description = "This PUT request call updates an existing agent.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Agent updated successfully.",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = AgentUserDTO.class))
                    }),
            @ApiResponse(responseCode = "404",
                    description = "Agent not found.",
                    content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<AgentUserDTO> updateAgent(@PathVariable Integer id, @RequestBody AgentUpdateDTO agentUserDTO) {
        return ResponseEntity.ok(userService.updateAgent(id, agentUserDTO));
    }

    @Operation(summary = "Create a new feedback",
            description = "This POST request call creates a new feedback.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Agent created successfully."),
            @ApiResponse(responseCode = "400",
                    description = "Bad request.",
                    content = @Content)
    })
    @PostMapping("/agent/feedback")
    public ResponseEntity<String> postFeedback(@RequestBody PostFeedbackDTO feedbackDTO) {
        return ResponseEntity.ok("Feedback enviado exitosamente \nFecha: " + feedbackDTO.getDate());
    }

    @Operation(summary = "Get performance", description = "This GET request call serves the purpose of returning performance by its id." )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Agent found.",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = AgentPerformance.class))
                    }),
            @ApiResponse(responseCode = "404",
                    description = "Agent not found.",
                    content = @Content),
    })
    @GetMapping("/agent/performance/{id}")
    public ResponseEntity<AgentPerformance> getAgentPerformance(@PathVariable("id") Integer id){
        AgentPerformance agentPerformance = userService.getAgentPerformance(id);
        return new ResponseEntity<>(agentPerformance, HttpStatus.OK);
    }

    @Operation(summary = "Create a new performance",
            description = "This POST request call creates a new performance.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Performance created successfully."),
            @ApiResponse(responseCode = "400",
                    description = "Bad request.",
                    content = @Content)
    })
    @PostMapping("/agent/performance/new")
    public ResponseEntity<String> addAgentPerformance(){
        AgentPerformance agentPerformance = new AgentPerformance();
        userService.addAgentPerformance(agentPerformance);
        return new ResponseEntity<>("Agent performance added", HttpStatus.OK);
    }

    @Operation(summary = "Delete performance by ID",
            description = "This DELETE request call deletes performance by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204",
                    description = "Performance deleted successfully."),
            @ApiResponse(responseCode = "404",
                    description = "Performance not found.",
                    content = @Content)
    })
    @DeleteMapping("/agent/performance/delete/{id}")
    public ResponseEntity<String> deleteAgentPerformance(@PathVariable("id") Integer id){
        userService.deleteAgentPerformance(id);
        return new ResponseEntity<>("Agent performance deleted", HttpStatus.OK);
    }

    @Operation(summary = "Update an existing performance",
            description = "This PUT request call updates an existing performance.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Performance updated successfully.",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = NoteDTO.class))
                    }),
            @ApiResponse(responseCode = "404",
                    description = "Performance not found.",
                    content = @Content)
    })
    @PutMapping("/agent/performance/update/{id}")
    public ResponseEntity<AgentPerformance> updateNotification(@PathVariable Integer id, @RequestBody AgentPerformance agentPerformance) {
        return ResponseEntity.ok(userService.updateAgentPerformance(id, agentPerformance));
    }

    @Operation(summary = "Get Feedback ", description = "This GET request call serves the purpose of returning feedback by id." )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Feedback found.",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = NoteDTO.class))
                    }),
            @ApiResponse(responseCode = "404",
                    description = "Feedback not found.",
                    content = @Content),
    })
    @GetMapping("/agent/feedback/{id}")
    public ResponseEntity<Note> getFeedback(@PathVariable("id") Integer id){
        Note note = feedbackService.getFeedbackById(id);
        return new ResponseEntity<>(note, HttpStatus.OK);
    }

    @Operation(summary = "Create a new feedback",
            description = "This POST request call creates a new feedback.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "feedback created successfully."),
            @ApiResponse(responseCode = "400",
                    description = "Bad request.",
                    content = @Content)
    })
    @PostMapping("/agent/feedback/new")
    public ResponseEntity<String> addFeedback(){
        Note note = new Note();
        feedbackService.addFeedback(note);
        return new ResponseEntity<>("Feedback added", HttpStatus.OK);
    }

    @Operation(summary = "Delete feedback by ID",
            description = "This DELETE request call deletes feedback by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204",
                    description = "feedback deleted successfully."),
            @ApiResponse(responseCode = "404",
                    description = "feedback not found.",
                    content = @Content)
    })
    @DeleteMapping("/agent/feedback/delete/{id}")
    public ResponseEntity<String> deleteFeedback(@PathVariable("id") Integer id){
        feedbackService.deleteFeedback(id);
        return new ResponseEntity<>("Feedback deleted", HttpStatus.OK);
    }

    @Operation(summary = "Update an existing feedback",
            description = "This PUT request call updates an existing feedback.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "feedback updated successfully.",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = NoteDTO.class))
                    }),
            @ApiResponse(responseCode = "404",
                    description = "Feedback not found.",
                    content = @Content)
    })
    @PutMapping("/agent/feedback/update/{id}")
    public ResponseEntity<Note> updateFeedback(@PathVariable Integer id, @RequestBody Note note) {
        return ResponseEntity.ok(feedbackService.updateFeedback(id, note));
    }

    /*
    * This enpoint gives a response directly from connect, without processing.*/
    @Operation(summary = "Get Filters ", description = "This GET request call serves the purpose of returning Filters by the instance id." )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Filters found.",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = DashboardFiltersDTO.class))
                    }),
            @ApiResponse(responseCode = "404",
                    description = "Filters not found.",
                    content = @Content),
    })
    @GetMapping("/list/{instanceId}")
    public ResponseEntity<DashboardFiltersDTO> getFilters(@PathVariable String instanceId) {
        DashboardFiltersDTO filters = agentListService.getAgentList(instanceId);

        System.out.println(instanceId);

        return ResponseEntity.ok(filters);
    }

    @Operation(summary = "Describe agent ", description = "This endpoint returns the details of an specific agent" )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "List found.",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = AgentDetailsDTO.class))
                    }),
            @ApiResponse(responseCode = "404",
                    description = "List not found.",
                    content = @Content),
    })
    @GetMapping("/detail/{instanceId}/{agentId}")
    public ResponseEntity<AgentDetailsDTO> getAgentDetails(@PathVariable String agentId,@PathVariable String instanceId) {
        System.out.println(agentId);
        System.out.println(instanceId);
        AgentDetailsDTO agent = agentListService.getAgentDetails(agentId, instanceId);

        System.out.println(agentId);

        return ResponseEntity.ok(agent);
    }

    private final AgentListService agentsService;

    @Operation(summary = "Get list of agents", description = "This endpoint returns a list of agents.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "List found.",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = AgentResponseDTO.class))
                    }),
            @ApiResponse(responseCode = "404",
                    description = "List not found.",
                    content = @Content),
    })
    @PostMapping("/agentslist")
    public Mono<AgentResponseDTO> getAllAgents(@RequestParam String instanceId) {
        return agentsService.getAllAgents(instanceId)
                .map(AgentResponseDTO::new);
    }

}
