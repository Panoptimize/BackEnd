package com.itesm.panoptimize.service;

import com.itesm.panoptimize.dto.queue.QueueCreateDTO;
import com.itesm.panoptimize.dto.queue.QueueDTO;
import com.itesm.panoptimize.dto.queue.QueueUpdateDTO;
import com.itesm.panoptimize.model.Queue;
import com.itesm.panoptimize.repository.QueueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class QueueService {

    private final QueueRepository queueRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public QueueService(QueueRepository queueRepository, ModelMapper modelMapper){
        this.queueRepository = queueRepository;
        this.modelMapper = modelMapper;
    }

    private QueueDTO convertToDTO(Queue queue) { return modelMapper.map(queue, QueueDTO.class); }

    private Queue convertToEntity(QueueCreateDTO queueCreateDTO){
        return modelMapper.map(queueCreateDTO, Queue.class);
    }

    public QueueDTO getQueue(String id){
        return convertToDTO(queueRepository.findById(id).orElse(null));
    }

    public Page<QueueDTO> getQueues(Pageable pageable) {
        return queueRepository.findAll(pageable).map(this::convertToDTO);
    }

    public QueueDTO createQueue(QueueCreateDTO queueDTO){
        if(queueRepository.existsById(queueDTO.getId())) {
            throw new IllegalArgumentException("Queue already exists");
        }
        return convertToDTO(queueRepository.save(convertToEntity(queueDTO)));
    }

    public void deleteQueue(String id){
        queueRepository.deleteById(id);
    }

    public QueueDTO updateQueue(String id, QueueUpdateDTO queueDTO){
        Queue queue = queueRepository.findById(id).orElse(null);
        if(queue == null) { return null; }
        if(queueDTO.getName() != null) { queue.setName(queueDTO.getName()); }

        return convertToDTO(queueRepository.save(queue));
    }

}
