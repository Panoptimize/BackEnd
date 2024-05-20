package com.itesm.panoptimize.service;

import com.itesm.panoptimize.dto.agent.AgentDTO;
import com.itesm.panoptimize.model.User;
import com.itesm.panoptimize.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public UserService(UserRepository userRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    public Page<AgentDTO> getAllAgents(Pageable pageable) {

        return userRepository.getUsersByType("agent", pageable).map(this::convertToDTO);
    }


    public User getUser(int id) {
        return userRepository.findById(id).orElse(null);
    }

    private AgentDTO convertToDTO(User agent) {
        return modelMapper.map(agent, AgentDTO.class);
    }

}
