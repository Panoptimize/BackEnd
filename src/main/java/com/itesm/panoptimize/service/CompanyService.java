package com.itesm.panoptimize.service;

import com.itesm.panoptimize.dto.company.CompanyDTO;
import com.itesm.panoptimize.model.Company;
import com.itesm.panoptimize.repository.CompanyRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyService {
    private final CompanyRepository companyRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public CompanyService(CompanyRepository companyRepository, ModelMapper modelMapper) {
        this.companyRepository = companyRepository;
        this.modelMapper = modelMapper;
    }

    public Page<CompanyDTO> getCompanies(Pageable pageable) {
        return companyRepository.findAll(pageable).map(this::convertToDTO);
    }

    public CompanyDTO getCompanyById(Integer companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new IllegalStateException(
                        "Company with id " + companyId + " does not exist"
                ));
        return convertToDTO(company);
    }

    public CompanyDTO addCompany(CompanyDTO company) {
        return convertToDTO(companyRepository.save(convertToEntity(company)));
    }

    public boolean deleteCompany(Integer companyId) {
        boolean exists = companyRepository.existsById(companyId);
        companyRepository.deleteById(companyId);
        return exists;
    }

    public CompanyDTO updateCompany(Integer companyId, CompanyDTO company) {
        Company companyToUpdate = companyRepository.findById(companyId)
                .orElseThrow(() -> new IllegalStateException(
                        "Company with id " + companyId + " does not exist"
                ));

        if (company.getName() != null) {
            companyToUpdate.setName(company.getName());
        }

        if (company.getLogoPath() != null) {
            companyToUpdate.setLogoPath(company.getLogoPath());
        }

        if (company.getSlogan() != null) {
            companyToUpdate.setSlogan(company.getSlogan());
        }

        companyRepository.save(companyToUpdate);
        return convertToDTO(companyToUpdate);
    }

    private CompanyDTO convertToDTO(Company company) {
        return modelMapper.map(company, CompanyDTO.class);
    }

    private Company convertToEntity(CompanyDTO companyDTO) {
        return modelMapper.map(companyDTO, Company.class);
    }
}
