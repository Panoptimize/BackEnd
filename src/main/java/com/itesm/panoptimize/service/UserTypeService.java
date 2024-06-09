package com.itesm.panoptimize.service;

import com.itesm.panoptimize.dto.usertype.UserTypeCreateDTO;
import com.itesm.panoptimize.dto.usertype.UserTypeDTO;
import com.itesm.panoptimize.dto.usertype.UserTypeUpdateDTO;
import com.itesm.panoptimize.model.UserType;
import com.itesm.panoptimize.repository.UserTypeRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserTypeService {
    private final UserTypeRepository userTypeRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public UserTypeService(UserTypeRepository userTypeRepository, ModelMapper modelMapper) {
        this.userTypeRepository = userTypeRepository;
        this.modelMapper = modelMapper;
    }

    private UserTypeDTO convertToDTO(UserType userType) {
        return modelMapper.map(userType, UserTypeDTO.class);
    }

    private UserType convertToEntity(UserTypeCreateDTO userTypeCreateDTO) {
        return modelMapper.map(userTypeCreateDTO, UserType.class);
    }

    public UserTypeDTO getUserType(Integer id) {
        return convertToDTO(userTypeRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User type not found")
        ));
    }

    public Page<UserTypeDTO> getAllUserTypes(Pageable pageable) {
        return userTypeRepository.findAll(pageable).map(this::convertToDTO);
    }

    public UserTypeDTO createUserType(UserTypeCreateDTO userTypeCreateDTO) {
        UserType userType = convertToEntity(userTypeCreateDTO);
        userType = userTypeRepository.save(userType);
        return convertToDTO(userType);
    }

    public UserTypeDTO updateUserType(Integer id, UserTypeUpdateDTO userTypeUpdateDTO) {
        UserType userType = userTypeRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User type not found")
        );

        if (userTypeUpdateDTO.getType() != null) {
            userType.setTypeName(userTypeUpdateDTO.getType());
        }

        return convertToDTO(userType);
    }

    public void deleteUserType(Integer id) {
        try {
            getUserType(id);
            userTypeRepository.deleteById(id);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User type not found", e);
        }
    }

    public UserTypeDTO getUserTypeBySecurityProfileId(String id) {
        return userTypeRepository.securityProfileId(id).map(this::convertToDTO).orElse(null);
    }
}
