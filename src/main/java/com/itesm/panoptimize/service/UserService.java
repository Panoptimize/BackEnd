package com.itesm.panoptimize.service;

import com.itesm.panoptimize.dto.agent.AgentDTO;
import com.itesm.panoptimize.model.AgentPerformance;
import com.itesm.panoptimize.model.User;
import com.itesm.panoptimize.repository.AgentPerformanceRepository;
import com.itesm.panoptimize.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final AgentPerformanceRepository agentPerformanceRepository;

    @Autowired
    public UserService(UserRepository userRepository, ModelMapper modelMapper, AgentPerformanceRepository agentPerformanceRepository) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.agentPerformanceRepository = agentPerformanceRepository;
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
    public AgentPerformance getAgentPerformance(int id) {
        return agentPerformanceRepository.findById(id).orElse(null);
    }

    public void addAgentPerformance(AgentPerformance agentPerformance) {
        agentPerformanceRepository.save(agentPerformance);
    }
    public void deleteAgentPerformance(int id) {
        agentPerformanceRepository.deleteById(id);
    }

    public AgentPerformance updateAgentPerformance(int id, AgentPerformance agentPerformance) {
        AgentPerformance agentPerformanceToUpdate = agentPerformanceRepository.findById(id)
                .orElse(null);
        if (agentPerformanceToUpdate != null) {
            agentPerformanceToUpdate.setAgent(agentPerformance.getAgent());
            agentPerformanceToUpdate.setDate(agentPerformance.getDate());
            agentPerformanceToUpdate.setTotalContactsHandled(agentPerformance.getTotalContactsHandled());
            agentPerformanceToUpdate.setTotalAfterCallWork(agentPerformance.getTotalAfterCallWork());
            agentPerformanceToUpdate.setAdherencePercentage(agentPerformance.getAdherencePercentage());
            agentPerformanceRepository.save(agentPerformanceToUpdate);
        }
        return agentPerformanceToUpdate;
    }

}
