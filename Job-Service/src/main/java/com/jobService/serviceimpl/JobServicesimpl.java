package com.jobService.serviceimpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jobService.model.JobEntity;
import com.jobService.repository.JobRepository;


@Service
public class JobServicesimpl{
	
	@Autowired
	private JobRepository jobRepository;
	
	 public List<JobEntity> findAll() {
	        return jobRepository.findAll();
	    }

	    public Optional<JobEntity> findById(long id) {
	        return jobRepository.findById(id);
	    }

	    public JobEntity save(JobEntity jobEntity) {
	        return jobRepository.save(jobEntity);
	    }

	    public void deleteById(long id) {
	        jobRepository.deleteById(id);
	    }
	    public List<JobEntity> findJobsByCompanyId(long companyId) {
	        return jobRepository.findByCompanyid(companyId);
	    }
}
