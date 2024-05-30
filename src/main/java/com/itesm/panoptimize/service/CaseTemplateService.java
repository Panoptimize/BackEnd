package com.itesm.panoptimize.service;

import com.itesm.panoptimize.dto.case_template.CaseTemplateDTO;
import com.itesm.panoptimize.dto.case_template.CreateCaseTemplateDTO;
import com.itesm.panoptimize.dto.case_template.UpdateCaseTemplateDTO;
import com.itesm.panoptimize.model.CaseTemplate;
import com.itesm.panoptimize.repository.CaseTemplateRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CaseTemplateService {
    private final CaseTemplateRepository caseTemplateRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public CaseTemplateService(CaseTemplateRepository caseTemplateRepository, ModelMapper modelMapper){
        this.caseTemplateRepository = caseTemplateRepository;
        this.modelMapper = modelMapper;
    }

    private CaseTemplateDTO convertToDTO(CaseTemplate caseTemplate) {
        return modelMapper.map(caseTemplate, CaseTemplateDTO.class);
    }

    private CaseTemplate convertToEntity(CreateCaseTemplateDTO caseTemplateDTO) {
        return modelMapper.map(caseTemplateDTO, CaseTemplate.class);
    }

    public CaseTemplateDTO getCaseTemplate(Integer id) {
        return convertToDTO(caseTemplateRepository.findById(id).orElse(null));
    }

    public CaseTemplateDTO createCaseTemplate(CreateCaseTemplateDTO caseTemplateDTO) {
        return convertToDTO(caseTemplateRepository.save(convertToEntity(caseTemplateDTO)));
    }

    public void deleteCaseTemplate(Integer id) {
        caseTemplateRepository.deleteById(id);
    }

    public Page<CaseTemplateDTO> getCaseTemplates(Pageable pageable) {
        return caseTemplateRepository.findAll(pageable).map(this::convertToDTO);
    }

    public CaseTemplateDTO caseTemplateDTO(Integer id, UpdateCaseTemplateDTO caseTemplateDTO) {
        CaseTemplate caseTemplate = caseTemplateRepository.findById(id).orElse(null);
        if (caseTemplate == null) {
            return null;
        }
        if (caseTemplateDTO.getName() != null) {
            caseTemplate.setName(caseTemplateDTO.getName());
        }
        return convertToDTO(caseTemplateRepository.save(caseTemplate));
    }

}
