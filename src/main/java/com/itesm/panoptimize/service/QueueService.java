package com.itesm.panoptimize.service;

import com.itesm.panoptimize.dto.queue.CreateQueueDTO;
import com.itesm.panoptimize.dto.queue.QueueDTO;
import com.itesm.panoptimize.dto.queue.UpdateQueueDTO;
import com.itesm.panoptimize.model.Queue;
import com.itesm.panoptimize.model.RoutingProfile;
import com.itesm.panoptimize.repository.QueueRepository;
import com.itesm.panoptimize.repository.RoutingProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class QueueService {

    private final QueueRepository queueRepository;
    private final ModelMapper modelMapper;
    private final RoutingProfileRepository routingProfileRepository;

    @Autowired
    public QueueService(QueueRepository queueRepository,
                        ModelMapper modelMapper,
                        RoutingProfileRepository routingProfileRepository){
        this.queueRepository = queueRepository;
        this.modelMapper = modelMapper;
        this.routingProfileRepository = routingProfileRepository;
    }

    private QueueDTO convertToDTO(Queue queue) { return modelMapper.map(queue, QueueDTO.class); }

    private Queue convertToEntity(CreateQueueDTO createQueueDTO){
        return modelMapper.map(createQueueDTO, Queue.class);
    }

    public QueueDTO getQueue(String id){
        Queue queue = queueRepository.findById(id).orElse(null);

        if (queue == null){
            return null;
        }

        return convertToDTO(queue);
    }

    public Page<QueueDTO> getQueues(Pageable pageable) {
        return queueRepository.findAll(pageable).map(this::convertToDTO);
    }

    public QueueDTO createQueue(CreateQueueDTO queueDTO){
        if(queueRepository.existsById(queueDTO.getId())) {
            throw new IllegalArgumentException("Queue already exists");
        }
        return convertToDTO(queueRepository.save(convertToEntity(queueDTO)));
    }

    public void deleteQueue(String id){
        queueRepository.deleteById(id);
    }

    public QueueDTO updateQueue(String id, UpdateQueueDTO queueDTO){
        Queue queue = queueRepository.findById(id).orElse(null);
        if(queue == null) { return null; }
        if(queueDTO.getName() != null) { queue.setName(queueDTO.getName()); }

        return convertToDTO(queueRepository.save(queue));
    }

    public void addRoutingProfiles(String id, Set<String> routingProfileIds){
        Queue queue = queueRepository.findById(id).orElse(null);
        if(queue == null) { return; }

        Set<RoutingProfile> routingProfiles = queue.getRoutingProfiles();
        routingProfiles.addAll(routingProfileRepository.findAllById(routingProfileIds));

        queue.setRoutingProfiles(routingProfiles);
        queueRepository.save(queue);

        routingProfiles.stream().map(RoutingProfile::getRoutingProfileId).collect(Collectors.toSet());
    }

}
