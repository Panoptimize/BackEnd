package com.itesm.panoptimize.service;

import com.itesm.panoptimize.model.Company;
import com.itesm.panoptimize.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyService {
    private final CompanyRepository companyRepository;

    @Autowired
    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public List<Company> getCompanies() {
        return companyRepository.findAll();
    }

    public Company getCompanyById(Integer companyId) {
        return companyRepository.findById(companyId)
                .orElseThrow(() -> new IllegalStateException(
                        "Company with id " + companyId + " does not exist"
                ));
    }

    public Company addCompany(Company company) {
        return companyRepository.save(company);
    }

    public boolean deleteCompany(Integer companyId) {
        boolean exists = companyRepository.existsById(companyId);
        companyRepository.deleteById(companyId);
        return exists;
    }

    public Company updateCompany(Integer companyId, Company company) {
        Company companyToUpdate = companyRepository.findById(companyId)
                .orElseThrow(() -> new IllegalStateException(
                        "Company with id " + companyId + " does not exist"
                ));
        companyToUpdate.setName(company.getName());
        companyToUpdate.setSlogan(company.getSlogan());
        companyToUpdate.setLogoPath(company.getLogoPath());
        companyRepository.save(companyToUpdate);
        return companyToUpdate;
    }

}
