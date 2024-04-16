package com.itesm.panoptimize.controller;

import com.itesm.panoptimize.dto.AgentFeedbackUpdateDTO;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;

public class AgentFeedbackUpdateController {
    @Operation(summary = "Edit the feedback from users", description = "Edit a feedback card from a specific user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Feedback card updated",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = AgentFeedbackUpdateDTO.class))
                    }),
            @ApiResponse(responseCode = "404",
                    description = "Something went wrong",
                    content = @Content),
    })


    @PatchMapping("/agent/feedback/edit")
    public ResponseEntity<AgentFeedbackUpdateDTO> editFeedback(AgentFeedbackUpdateDTO feed){
        return ResponseEntity.ok(feed);
    }
}
