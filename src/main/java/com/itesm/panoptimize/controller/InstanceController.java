package com.itesm.panoptimize.controller;

import com.itesm.panoptimize.dto.instance.InstanceDTO;
import com.itesm.panoptimize.model.Instance;
import com.itesm.panoptimize.service.InstanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path="/instance")
public class InstanceController implements Mappable<Instance, InstanceDTO> {
    private final InstanceService instanceService;
    private final ModelMapper modelMapper;

    @Autowired
    public InstanceController(InstanceService instanceService, ModelMapper modelMapper) {
        this.instanceService = instanceService;
        this.modelMapper = modelMapper;
    }

    @Override
    public InstanceDTO convertToDTO(Instance instance) {
        return modelMapper.map(instance, InstanceDTO.class);
    }

    @Override
    public Instance convertToEntity(InstanceDTO instanceDTO) {
        return modelMapper.map(instanceDTO, Instance.class);
    }

    @Operation(summary = "Get all instances")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found all instances",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = InstanceDTO.class))),
            @ApiResponse(responseCode = "404", description = "No instances found")
    })
    @GetMapping(path="/")
    public ResponseEntity<List<Instance>> getInstances() {
        List<Instance> instances = instanceService.getInstances();
        if (instances.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(instances);
    }

    @Operation(summary = "Get instance by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the instance",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = InstanceDTO.class))),
            @ApiResponse(responseCode = "404", description = "No instance found")
    })
    @GetMapping(path="/{instanceId}")
    public ResponseEntity<InstanceDTO> getInstanceById(@PathVariable String instanceId) {
        // Convert the instance to an instanceDTO
        try {
            return ResponseEntity.ok(convertToDTO(instanceService.getInstanceById(instanceId)));
        } catch (IllegalStateException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Delete instance by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Instance deleted"),
            @ApiResponse(responseCode = "404", description = "No instance found")
    })
    @DeleteMapping(path="/{instanceId}")
    public ResponseEntity<String> deleteInstance(@PathVariable String instanceId) {
        try {
            instanceService.deleteInstance(instanceId);
            return ResponseEntity.ok("Instance with id " + instanceId + " deleted");
        } catch (IllegalStateException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Add a new instance")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Instance added"),
            @ApiResponse(responseCode = "400", description = "Invalid instance")
    })
    @PostMapping(path="/")
    public ResponseEntity<String> addInstance(@RequestBody InstanceDTO instanceDTO) {
        instanceService.addNewInstance(this.convertToEntity(instanceDTO));
        return ResponseEntity.ok("Instance added");
    }
}
