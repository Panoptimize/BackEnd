package com.itesm.panoptimize.service;

import com.itesm.panoptimize.dto.agent.AgentDTO;
import com.itesm.panoptimize.model.User;
import com.itesm.panoptimize.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllAgents(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);

        return userRepository.getUsersByType("agent", pageRequest);
    }

    public User getAgent(int id) {
        return userRepository.findById(id).orElse(null);
    }

}
