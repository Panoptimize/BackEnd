package com.itesm.panoptimize.service;

import com.itesm.panoptimize.model.Instance;
import com.itesm.panoptimize.repository.InstanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InstanceService {
    private final InstanceRepository instanceRepository;

    @Autowired
    public InstanceService(InstanceRepository instanceRepository) {
        this.instanceRepository = instanceRepository;
    }

    public List<Instance> getInstances() {
        return instanceRepository.findAll();
    }

    public void addNewInstance(Instance instance) {
        instanceRepository.save(instance);
    }

    public Instance getInstanceById(String instanceId) {
        Optional<Instance> instance = instanceRepository.findById(instanceId);
        if (instance.isEmpty()) {
            throw new IllegalStateException("Instance with id " + instanceId + " does not exist");
        }
        return instance.get();
    }

    public void deleteInstance(String instanceId) {
        boolean exists = instanceRepository.existsById(instanceId);
        if (!exists) {
            throw new IllegalStateException("Instance with id " + instanceId + " does not exist");
        }
        instanceRepository.deleteById(instanceId);
    }
}
