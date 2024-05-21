package com.itesm.panoptimize.service;

import com.itesm.panoptimize.dto.company.CompanyDTO;
import com.itesm.panoptimize.dto.global_metric.GlobalMetricCreateDTO;
import com.itesm.panoptimize.dto.global_metric.GlobalMetricDTO;
import com.itesm.panoptimize.dto.global_metric.GlobalMetricUpdateDTO;
import com.itesm.panoptimize.model.Company;
import com.itesm.panoptimize.model.GlobalMetric;
import com.itesm.panoptimize.repository.GlobalMetricRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GlobalMetricService {
    private final GlobalMetricRepository globalMetricRepository;
    private final ModelMapper modelMapper;
    private final CompanyService companyService;

    @Autowired
    public GlobalMetricService(GlobalMetricRepository globalMetricRepository, ModelMapper modelMapper, CompanyService companyService) {
        this.globalMetricRepository = globalMetricRepository;
        this.modelMapper = modelMapper;
        this.companyService = companyService;
    }

    public Page<GlobalMetricDTO> getGlobalMetrics(Pageable pageable) {

        return globalMetricRepository.findAll(pageable).map(this::convertToDTO);
    }

    public GlobalMetricDTO getGlobalMetricById(Integer id) {

        GlobalMetric globalMetric = globalMetricRepository.findById(id).orElse(null);
        if (globalMetric == null) {
            return null;
        }

        return convertToDTO(globalMetric);
    }

    public GlobalMetricDTO getGlobalMetricByDescription(String description) {

        GlobalMetric globalMetric = globalMetricRepository.findByMetricDescription(description).orElse(null);
        if (globalMetric == null) {
            return null;
        }

        return convertToDTO(globalMetric);
    }

    public GlobalMetricDTO createGlobalMetric(GlobalMetricCreateDTO globalMetric) {
        GlobalMetric newGlobalMetric = convertToEntity(globalMetric);
        newGlobalMetric.setId(null);

        try {
            return convertToDTO(globalMetricRepository.save(newGlobalMetric));
        } catch (Exception e) {
            return null;
        }
    }

    public GlobalMetricUpdateDTO updateGlobalMetric(Integer id, GlobalMetricUpdateDTO globalMetric) {
        GlobalMetric existingGlobalMetric = globalMetricRepository.findById(id).orElse(null);
        if (existingGlobalMetric == null) {
            return null;
        }

        if(globalMetric.getMetricDescription() != null) {
            existingGlobalMetric.setMetricDescription(globalMetric.getMetricDescription());
        }

        if(globalMetric.getCurrentValue() != null) {
            existingGlobalMetric.setCurrentValue(globalMetric.getCurrentValue());
        }

        if(globalMetric.getStatus() != null) {
            existingGlobalMetric.setStatus(globalMetric.getStatus());
        }

        if(globalMetric.getCompanyId() != null) {
            existingGlobalMetric.setCompany(convertToEntity(companyService.getCompanyById(globalMetric.getCompanyId())));
        }

        return convertToUpdateDTO(globalMetricRepository.save(existingGlobalMetric));
    }

    private GlobalMetricDTO convertToDTO(GlobalMetric globalMetric) {
        return modelMapper.map(globalMetric, GlobalMetricDTO.class);
    }

    private GlobalMetric convertToEntity(GlobalMetricCreateDTO globalMetricDTO) {
        return modelMapper.map(globalMetricDTO, GlobalMetric.class);
    }

    private GlobalMetricUpdateDTO convertToUpdateDTO(GlobalMetric globalMetric) {
        return modelMapper.map(globalMetric, GlobalMetricUpdateDTO.class);
    }

    private Company convertToEntity(CompanyDTO companyDTO) {
        return modelMapper.map(companyDTO, Company.class);
    }

}
