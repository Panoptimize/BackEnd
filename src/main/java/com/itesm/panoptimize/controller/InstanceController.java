package com.itesm.panoptimize.controller;

import com.itesm.panoptimize.dto.instance.InstanceDTO;
import com.itesm.panoptimize.model.Instance;
import com.itesm.panoptimize.service.InstanceService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping(path="/instance")
public class InstanceController {
    private final InstanceService instanceService;
    private final ModelMapper modelMapper;

    @Autowired
    public InstanceController(InstanceService instanceService, ModelMapper modelMapper) {
        this.instanceService = instanceService;
        this.modelMapper = modelMapper;
    }

    private InstanceDTO convertToDTO(Instance instance) {
        return modelMapper.map(instance, InstanceDTO.class);
    }

    private Instance convertToEntity(InstanceDTO instanceDTO) {
        return modelMapper.map(instanceDTO, Instance.class);
    }

    @GetMapping(path="/")
    public ResponseEntity<List<Instance>> getInstances() {
        // Convert the list of instances to a list of instanceDTOs
        return ResponseEntity.ok(instanceService.getInstances());
    }

    @GetMapping(path="/{instanceId}")
    public ResponseEntity<InstanceDTO> getInstanceById(@PathVariable String instanceId) {
        // Convert the instance to an instanceDTO
        try {
            return ResponseEntity.ok(convertToDTO(instanceService.getInstanceById(instanceId)));
        } catch (IllegalStateException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(path="/{instanceId}/delete")
    public ResponseEntity<String> deleteInstance(@PathVariable String instanceId) {
        try {
            instanceService.deleteInstance(instanceId);
            return ResponseEntity.ok("Instance with id " + instanceId + " deleted");
        } catch (IllegalStateException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(path="/add")
    public ResponseEntity<String> addInstance(@RequestBody InstanceDTO instanceDTO) {
        instanceService.addNewInstance(this.convertToEntity(instanceDTO));
        return ResponseEntity.ok("Instance added");
    }
}
