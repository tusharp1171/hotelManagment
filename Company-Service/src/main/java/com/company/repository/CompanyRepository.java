package com.company.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.company.model.CompanyEntity;
@Repository
public interface CompanyRepository extends JpaRepository<CompanyEntity, Long>{

}
