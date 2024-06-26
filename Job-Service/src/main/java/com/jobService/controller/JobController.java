package com.jobService.controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.jobService.model.JobEntity;
import com.jobService.serviceimpl.JobServicesimpl;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/jobs")
@Validated
public class JobController {

    @Autowired
    private JobServicesimpl jobService;

    @GetMapping
    public ResponseEntity<List<JobEntity>> getAllJobs() {
        List<JobEntity> jobs = jobService.findAll();
        return new ResponseEntity<>(jobs, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getJobById(@PathVariable long id) {
        Optional<JobEntity> job = jobService.findById(id);
        if (job.isPresent()) {
            return new ResponseEntity<>(job.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(createResponse("Job not found for get by id", HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<?> createJob(@Valid @RequestBody JobEntity jobEntity) {
        JobEntity savedJob = jobService.save(jobEntity);
        return new ResponseEntity<>(createResponse("Job created successfully", HttpStatus.CREATED, savedJob), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateJob(@PathVariable long id, @Valid @RequestBody JobEntity jobEntity) {
        if (jobService.findById(id).isPresent()) {
            jobEntity.setId(id);
            JobEntity updatedJob = jobService.save(jobEntity);
            return new ResponseEntity<>(createResponse("Job updated successfully", HttpStatus.OK, updatedJob), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(createResponse("Job not found for update ", HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteJob(@PathVariable long id) {
        if (jobService.findById(id).isPresent()) {
            jobService.deleteById(id);
            return new ResponseEntity<>(createResponse("Job deleted successfully", HttpStatus.OK), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(createResponse("Job not found for delete ", HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/byCompany/{companyId}")
    public ResponseEntity<?> getJobsByCompanyId(@PathVariable long companyId) {
        List<JobEntity> jobs = jobService.findJobsByCompanyId(companyId);
        if (!jobs.isEmpty()) {
            return new ResponseEntity<>(jobs, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(createResponse("No jobs found for company id: " + companyId, HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
        }
    }

    private Map<String, Object> createResponse(String message, HttpStatus status) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", status.value());
        response.put("message", message);
        return response;
    }

    private Map<String, Object> createResponse(String message, HttpStatus status, Object data) {
        Map<String, Object> response = createResponse(message, status);
        response.put("data", data);
        return response;
    }
}
