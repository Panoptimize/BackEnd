package com.itesm.panoptimize.service;

import com.itesm.panoptimize.dto.routing_profile.CreateRoutingProfileDTO;
import com.itesm.panoptimize.dto.routing_profile.RoutingProfileDTO;
import com.itesm.panoptimize.dto.routing_profile.UpdateRoutingProfileDTO;
import com.itesm.panoptimize.model.RoutingProfile;
import com.itesm.panoptimize.repository.RoutingProfileRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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
        return convertToDTO(routingProfileRepository.save(convertToEntity(routingProfileDTO)));
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
