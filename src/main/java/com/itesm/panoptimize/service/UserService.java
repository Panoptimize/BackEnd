package com.itesm.panoptimize.service;

import com.itesm.panoptimize.dto.agent.AgentDTO;
import com.itesm.panoptimize.dto.supervisor.SupervisorCreateDTO;
import com.itesm.panoptimize.dto.supervisor.SupervisorDTO;
import com.itesm.panoptimize.dto.supervisor.SupervisorUserDTO;
import com.itesm.panoptimize.model.AgentPerformance;
import com.itesm.panoptimize.model.User;
import com.itesm.panoptimize.repository.AgentPerformanceRepository;
import com.itesm.panoptimize.repository.UserRepository;
import com.itesm.panoptimize.repository.UserTypeRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserTypeRepository userTypeRepository;
    private final ModelMapper modelMapper;
    private final AgentPerformanceRepository agentPerformanceRepository;

    public UserService(UserRepository userRepository, ModelMapper modelMapper, AgentPerformanceRepository agentPerformanceRepository, UserTypeRepository userTypeRepository) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.agentPerformanceRepository = agentPerformanceRepository;
        this.userTypeRepository = userTypeRepository;
    }

    private AgentDTO convertToAgentDTO(User agent) {
        return modelMapper.map(agent, AgentDTO.class);
    }

    private User convertToEntity(SupervisorCreateDTO supervisorCreateDTO) {
        return modelMapper.map(supervisorCreateDTO, User.class);
    }

    private SupervisorUserDTO convertToSupervisorDTO(User supervisor) {
        return modelMapper.map(supervisor, SupervisorUserDTO.class);
    }
    public Page<AgentDTO> getAllAgents(Pageable pageable) {
        return userRepository.getUsersByType("agent", pageable).map(this::convertToAgentDTO);
    }

    public Page<SupervisorUserDTO> getAllSupervisors(Pageable pageable) {
        return userRepository.getUsersByType("supervisor", pageable).map(this::convertToSupervisorDTO);
    }

    public User getUser(int id) {
        return userRepository.findById(id).orElse(null);
    }

    public SupervisorUserDTO createSupervisor(SupervisorCreateDTO supervisorCreateDTO) {
        User supervisor = modelMapper.map(supervisorCreateDTO, User.class);
        supervisor.setUserType(userTypeRepository.typeName("supervisor"));
        return convertToSupervisorDTO(userRepository.save(supervisor));
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
