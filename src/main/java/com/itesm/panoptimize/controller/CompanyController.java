package com.itesm.panoptimize.controller;

import com.itesm.panoptimize.dto.company.CompanyDTO;
import com.itesm.panoptimize.model.Company;
import com.itesm.panoptimize.service.CompanyService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path="/company")
public class CompanyController implements Mappable<Company, CompanyDTO> {

    private final CompanyService companyService;
    private final ModelMapper modelMapper;

    @Autowired
    public CompanyController(CompanyService companyService, ModelMapper modelMapper) {
        this.companyService = companyService;
        this.modelMapper = modelMapper;
    }


    @GetMapping(path="/")
    public ResponseEntity<List<CompanyDTO>> getCompanies() {
        List<CompanyDTO> companies = companyService.getCompanies().stream().map(this::convertToDTO).toList();
        if (companies.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(companies);
    }

    @GetMapping(path="/{companyId}")
    public ResponseEntity<CompanyDTO> getCompanyById(@PathVariable Integer companyId) {
        Company company = companyService.getCompanyById(companyId);
        return ResponseEntity.ok(convertToDTO(company));
    }

    @PostMapping(path="/")
    public ResponseEntity<CompanyDTO> addCompany(@RequestBody CompanyDTO company) {
        CompanyDTO newCompany = convertToDTO(companyService.addCompany(convertToEntity(company)));
        return ResponseEntity.ok(newCompany);
    }

    @DeleteMapping(path="/{companyId}")
    public ResponseEntity<Boolean> deleteCompany(@PathVariable Integer companyId) {
        return ResponseEntity.ok(
                companyService.deleteCompany(companyId)
        );
    }

    @Override
    public CompanyDTO convertToDTO(Company company) {
        return modelMapper.map(company, CompanyDTO.class);
    }

    @Override
    public Company convertToEntity(CompanyDTO companyCreateDTO) {
        return modelMapper.map(companyCreateDTO, Company.class);
    }

    @PatchMapping(path="/{companyId}")
    public ResponseEntity<CompanyDTO> updateCompany(@PathVariable Integer companyId, @RequestBody CompanyDTO company) {
        return ResponseEntity.ok(convertToDTO(companyService.updateCompany(companyId, convertToEntity(company))));
    }
}
