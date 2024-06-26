package com.company.service;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.company.model.CompanyEntity;
import com.company.repository.CompanyRepository;


@Service
public class CompanyService {

    @Autowired
    private CompanyRepository companyRepository;

    public List<CompanyEntity> getAllCompanies() {
        return companyRepository.findAll();
    }

    public Optional<CompanyEntity> getCompanyById(long id) {
        return companyRepository.findById(id);
    }

    public CompanyEntity save(CompanyEntity companyEntity) {
        return companyRepository.save(companyEntity);
    }

    public void deleteById(long id) {
        companyRepository.deleteById(id);
    }

}