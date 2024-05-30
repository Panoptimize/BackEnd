package com.itesm.panoptimize.controller;

import com.itesm.panoptimize.dto.queue.QueueCreateDTO;
import com.itesm.panoptimize.dto.queue.QueueDTO;
import com.itesm.panoptimize.dto.queue.QueueUpdateDTO;
import com.itesm.panoptimize.service.QueueService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/queue")
public class QueueController {

    private final QueueService queueService;

    public QueueController(QueueService queueService){
        this.queueService = queueService;
    }

    @Operation(summary = "Get queue by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Queue found"),
            @ApiResponse(responseCode = "404", description = "Queue not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<QueueDTO> getQueue(@PathVariable String id){
        return ResponseEntity.ok(queueService.getQueue(id));
    }

    @Operation(summary = "Get all queues")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Queues found"),
            @ApiResponse(responseCode = "404", description = "Queues not found")
    })
    @GetMapping("/")
    public ResponseEntity<Page<QueueDTO>> getQueues(Pageable pageable){
        return ResponseEntity.ok(queueService.getQueues(pageable));
    }

    @Operation(summary = "Create queue")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Queue created"),
            @ApiResponse(responseCode = "404", description = "Queue not created")
    })
    @PostMapping("/")
    public ResponseEntity<QueueDTO> createQueue(@RequestBody QueueCreateDTO queueCreateDTO){
        return ResponseEntity.ok(queueService.createQueue(queueCreateDTO));
    }

    @Operation(summary = "Delete queue by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Queue deleted"),
            @ApiResponse(responseCode = "404", description = "Queue not deleted")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteQueue(@PathVariable String id){
        queueService.deleteQueue(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Update queue")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Queue updated"),
            @ApiResponse(responseCode = "404", description = "Queue not updated")
    })
    @PutMapping("/{id}")
    public ResponseEntity<QueueDTO> getQueue(@PathVariable String id, @RequestBody QueueUpdateDTO queueUpdateDTO){
        return ResponseEntity.ok(queueService.updateQueue(id, queueUpdateDTO));
    }

}
