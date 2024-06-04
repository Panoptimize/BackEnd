package com.itesm.panoptimize.service;

import com.itesm.panoptimize.dto.routingprofile.CreateRoutingProfileDTO;
import com.itesm.panoptimize.dto.routingprofile.RoutingProfileDTO;
import com.itesm.panoptimize.dto.routingprofile.UpdateRoutingProfileDTO;
import com.itesm.panoptimize.model.Company;
import com.itesm.panoptimize.model.Queue;
import com.itesm.panoptimize.model.RoutingProfile;
import com.itesm.panoptimize.repository.RoutingProfileRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class RoutingProfileService {
    private final RoutingProfileRepository routingProfileRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public RoutingProfileService(RoutingProfileRepository routingProfileRepository, ModelMapper modelMapper){
        this.routingProfileRepository = routingProfileRepository;
        this.modelMapper = modelMapper;
    }

    private RoutingProfileDTO convertToDTO(RoutingProfile routingProfile) {
        return modelMapper.map(routingProfile, RoutingProfileDTO.class);
    }

    private RoutingProfile convertToEntity(CreateRoutingProfileDTO createRoutingProfileDTO) {
        return modelMapper.map(createRoutingProfileDTO, RoutingProfile.class);
    }

    public RoutingProfileDTO getRoutingProfile(String id){
        return convertToDTO(routingProfileRepository.findById(id).orElse(null));
    }

    public Page<RoutingProfileDTO> getRoutingProfiles(Pageable pageable){
        return routingProfileRepository.findAll(pageable).map(this::convertToDTO);
    }

    public RoutingProfileDTO createRoutingProfile(CreateRoutingProfileDTO routingProfileDTO){
        if(routingProfileRepository.existsById(routingProfileDTO.getId())) {
            throw new IllegalArgumentException("Routing Profile already exists");
        }

        RoutingProfile routingProfileToCreate = new RoutingProfile();
        routingProfileToCreate.setName(routingProfileDTO.getName());
        routingProfileToCreate.setRoutingProfileId(routingProfileDTO.getId());

        Company company = new Company();
        company.setId(routingProfileDTO.getCompanyId());
        routingProfileToCreate.setCompany(company);

        Set<Queue> queues = new HashSet<>();
        routingProfileDTO.getQueues().forEach(queue -> {
            Queue queueToAdd = new Queue();
            queueToAdd.setId(queue);
            queues.add(queueToAdd);
        });

        routingProfileToCreate.setQueues(queues);

        return convertToDTO(routingProfileRepository.save(routingProfileToCreate));
    }

    public void deleteRoutingProfile(String id){
        routingProfileRepository.deleteById(id);
    }

    public RoutingProfileDTO updateRoutingProfile(String id, UpdateRoutingProfileDTO routingProfileDTO){
        RoutingProfile routingProfile = routingProfileRepository.findById(id).orElse(null);
        if(routingProfile == null) { return null; }
        if(routingProfileDTO.getName() != null) { routingProfile.setName(routingProfileDTO.getName()); }

        return convertToDTO(routingProfileRepository.save(routingProfile));
    }

}
