package com.itesm.panoptimize.controller;

import com.itesm.panoptimize.dto.company.CompanyDTO;
import com.itesm.panoptimize.model.Company;
import com.itesm.panoptimize.service.CompanyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path="/company")
public class CompanyController {

    private final CompanyService companyService;
    private final ModelMapper modelMapper;

    @Autowired
    public CompanyController(CompanyService companyService, ModelMapper modelMapper) {
        this.companyService = companyService;
        this.modelMapper = modelMapper;
    }

    @Operation(summary = "Get all companies")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found all companies",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = CompanyDTO[].class))),
            @ApiResponse(responseCode = "404", description = "No companies found")
    })
    @GetMapping(path="/")
    public ResponseEntity<Page<CompanyDTO>> getCompanies(Pageable pageable) {
        Page<CompanyDTO> companies = companyService.getCompanies(pageable);
        if (companies.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(companies);
    }

    @Operation(summary = "Get company by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the company",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = CompanyDTO.class))),
            @ApiResponse(responseCode = "404", description = "No company found")
    })
    @GetMapping(path="/{companyId}")
    public ResponseEntity<CompanyDTO> getCompanyById(@PathVariable Integer companyId) {
        CompanyDTO company = companyService.getCompanyById(companyId);
        return ResponseEntity.ok(company);
    }

    @Operation(summary = "Add a new company")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Company added",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = CompanyDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid company")
    })
    @PostMapping(path="/")
    public ResponseEntity<CompanyDTO> addCompany(@RequestBody CompanyDTO company) {
        CompanyDTO newCompany = companyService.addCompany(company);
        return ResponseEntity.ok(newCompany);
    }

    @DeleteMapping(path="/{companyId}")
    public ResponseEntity<Boolean> deleteCompany(@PathVariable Integer companyId) {
        return ResponseEntity.ok(
                companyService.deleteCompany(companyId)
        );
    }

    @PutMapping(path="/{companyId}")
    public ResponseEntity<CompanyDTO> updateCompany(@PathVariable Integer companyId, @RequestBody CompanyDTO company) {
        return ResponseEntity.ok(companyService.updateCompany(companyId, company));
    }
}
