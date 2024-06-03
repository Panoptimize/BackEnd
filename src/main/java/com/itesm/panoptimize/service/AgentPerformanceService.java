package com.itesm.panoptimize.service;

import com.itesm.panoptimize.dto.agent_performance.Agent_PerformanceDTO;
import com.itesm.panoptimize.dto.agent_performance.CreateAgent_PerformanceDTO;
import com.itesm.panoptimize.dto.agent_performance.UpdateAgent_PerformanceDTO;
import com.itesm.panoptimize.dto.note.CreateNoteDTO;
import com.itesm.panoptimize.dto.note.NoteDTO;
import com.itesm.panoptimize.dto.note.UpdateNoteDTO;
import com.itesm.panoptimize.dto.performance.AgentPerformanceDTO;
import com.itesm.panoptimize.model.AgentPerformance;
import com.itesm.panoptimize.model.Note;
import com.itesm.panoptimize.model.User;
import com.itesm.panoptimize.repository.AgentPerformanceRepository;
import com.itesm.panoptimize.repository.NoteRepository;
import com.itesm.panoptimize.repository.UserRepository;
import org.aspectj.weaver.loadtime.Agent;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AgentPerformanceService {
    private final AgentPerformanceRepository agentPerformanceRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public AgentPerformanceService(AgentPerformanceRepository agentPerformanceRepository, ModelMapper modelMapper, UserRepository userRepository) {
        this.modelMapper = modelMapper;
        this.agentPerformanceRepository = agentPerformanceRepository;
        this.userRepository = userRepository;
    }

    private Agent_PerformanceDTO convertToDTO(AgentPerformance agentPerformance) {
        return modelMapper.map(agentPerformance, Agent_PerformanceDTO.class);
    }

    private AgentPerformance convertDTOToEntity(CreateAgent_PerformanceDTO createAgentPerformanceDTO) {
        return modelMapper.map(createAgentPerformanceDTO, AgentPerformance.class);
    }

    public Agent_PerformanceDTO getAgentPerformance(Integer id) {
        return convertToDTO(agentPerformanceRepository.findById(id).orElse(null));
    }

    public Page<Agent_PerformanceDTO> getAgentPerformances(Pageable pageable) {
        return agentPerformanceRepository.findAll(pageable).map(this::convertToDTO);
    }

    public void deleteAgentPerformance(Integer id) {
        agentPerformanceRepository.deleteById(id);
    }

    public Agent_PerformanceDTO createAgentPerformance(CreateAgent_PerformanceDTO createAgentPerformanceDTO) {
        AgentPerformance agentPerformanceToCreate = new AgentPerformance();
        agentPerformanceToCreate.setAvgAfterCallWorkTime(createAgentPerformanceDTO.getAvgAfterContactWorkTime());
        agentPerformanceToCreate.setAvgAbandonTime(createAgentPerformanceDTO.getAvgAbandonTime());
        agentPerformanceToCreate.setAvgHandleTime(createAgentPerformanceDTO.getAvgAfterContactWorkTime());
        agentPerformanceToCreate.setAvgHoldTime(createAgentPerformanceDTO.getAvgAfterContactWorkTime());

        User agent = userRepository.findById(createAgentPerformanceDTO.getAgentId()).orElse(null);
        if(agent == null) {throw new IllegalArgumentException("Agent not found");}
        agentPerformanceToCreate.setAgent(agent);

        return convertToDTO(agentPerformanceRepository.save(agentPerformanceToCreate));
    }

    public Agent_PerformanceDTO updateAgentPerformance(Integer id, UpdateAgent_PerformanceDTO updateAgentPerformanceDTO) {
        AgentPerformance agentPerformance = agentPerformanceRepository.findById(id).orElse(null);
        if (agentPerformance == null) {
            return null;
        }
        if (updateAgentPerformanceDTO.getAgentId() != null) {
            agentPerformance.setAgent(userRepository.findById(updateAgentPerformanceDTO.getAgentId()).orElse(null));
        }
        if (updateAgentPerformanceDTO.getAvgAbandonTime() != null) {
            agentPerformance.setAvgAbandonTime(updateAgentPerformanceDTO.getAvgAbandonTime());
        }
        if (updateAgentPerformanceDTO.getAvgAfterContactWorkTime() != null) {
            agentPerformance.setAvgAfterCallWorkTime(updateAgentPerformanceDTO.getAvgAfterContactWorkTime());
        }
        if(updateAgentPerformanceDTO.getAvgHandleTime() != null) {
            agentPerformance.setAvgHandleTime(updateAgentPerformanceDTO.getAvgHandleTime());
        }
        if(updateAgentPerformanceDTO.getAvgHoldTime() != null) {
            agentPerformance.setAvgHoldTime(updateAgentPerformanceDTO.getAvgHoldTime());
        }

        return convertToDTO(agentPerformanceRepository.save(agentPerformance));
    }
}
