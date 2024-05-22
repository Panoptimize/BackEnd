package com.itesm.panoptimize.service;

import com.itesm.panoptimize.dto.instance.InstanceDTO;
import com.itesm.panoptimize.model.Instance;
import com.itesm.panoptimize.repository.InstanceRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InstanceService {
    private final InstanceRepository instanceRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public InstanceService(InstanceRepository instanceRepository, ModelMapper modelMapper) {
        this.instanceRepository = instanceRepository;
        this.modelMapper = modelMapper;
    }

    public List<InstanceDTO> getInstances() {
        return instanceRepository.findAll().stream().map(this::convertToDTO).toList();
    }

    public InstanceDTO addNewInstance(InstanceDTO instance) {

        return convertToDTO(instanceRepository.save(convertToEntity(instance)));
    }

    public InstanceDTO getInstanceById(String instanceId) {
        Optional<Instance> instance = instanceRepository.findById(instanceId);
        if (instance.isEmpty()) {
            throw new IllegalStateException("Instance with id " + instanceId + " does not exist");
        }
        return convertToDTO(instance.get());
    }

    public boolean deleteInstance(String instanceId) {
        boolean exists = instanceRepository.existsById(instanceId);
        if (exists)
            instanceRepository.deleteById(instanceId);
        return exists;
    }

    private InstanceDTO convertToDTO(Instance instance) {
        return modelMapper.map(instance, InstanceDTO.class);
    }

    private Instance convertToEntity(InstanceDTO instanceDTO) {
        return modelMapper.map(instanceDTO, Instance.class);
    }

}
