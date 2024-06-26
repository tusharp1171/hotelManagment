package com.jobService.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jobService.model.JobEntity;

@Repository
public interface JobRepository extends JpaRepository<JobEntity, Long>{
	
	
	 List<JobEntity> findByCompanyid(long companyId);

}
