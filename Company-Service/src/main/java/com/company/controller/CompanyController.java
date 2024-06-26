package com.company.controller;
import com.company.model.CompanyEntity;
import com.company.model.JobDTO;
import com.company.model.ReviewsDTO;
import com.company.service.CompanyService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/companies")
@Validated
public class CompanyController {

	@Autowired
	private RestTemplate restTemplate;
    @Autowired
    private CompanyService companyService;

    @GetMapping
    public ResponseEntity<List<CompanyEntity>> getAllCompanies() {
        List<CompanyEntity> companies = companyService.getAllCompanies();
        return new ResponseEntity<>(companies, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCompanyByIdonly(@PathVariable long id) {
        Optional<CompanyEntity> company = companyService.getCompanyById(id);
        if (company.isPresent()) {
        	
        	
            return new ResponseEntity<>(company.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(createResponse("Company not found for get by id", HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
        }
    }
    
    @DeleteMapping("deleteCompany/{id}")
    public ResponseEntity<String> deleteCompanyById(@PathVariable long id) {
        Optional<CompanyEntity> company = companyService.getCompanyById(id);
        if (company.isPresent()) {
            long companyId = company.get().getId();

            // Fetch and delete reviews associated with the company
            try {
                ResponseEntity<ReviewsDTO[]> reviewResponse = restTemplate.getForEntity(
                        "http://localhost:9090/api/reviews/byCompany/" + companyId,
                        ReviewsDTO[].class);

                if (reviewResponse.getStatusCode().is2xxSuccessful() && reviewResponse.getBody() != null) {
                    List<ReviewsDTO> listReview = Arrays.asList(reviewResponse.getBody());
                    for (ReviewsDTO review : listReview) {
                        long deleteId = review.getId();
                        try {
                            restTemplate.delete("http://localhost:9090/api/reviews/" + deleteId);
                        } catch (RestClientException e) {
                            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                    .body("Failed to delete review with id: " + deleteId + ". Error: " + e.getMessage());
                        }
                    }
                }
            } catch (RestClientException e) {
                if (!e.getMessage().contains("404")) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body("Error fetching reviews for company id: " + companyId + ". Error: " + e.getMessage());
                }
            }

            // Fetch and delete jobs associated with the company
            try {
                ResponseEntity<JobDTO[]> jobResponse = restTemplate.getForEntity(
                        "http://localhost:9090/api/jobs/byCompany/" + companyId,
                        JobDTO[].class);

                if (jobResponse.getStatusCode().is2xxSuccessful() && jobResponse.getBody() != null) {
                    List<JobDTO> listJob = Arrays.asList(jobResponse.getBody());
                    for (JobDTO job : listJob) {
                        long deleteId = job.getId();
                        try {
                            restTemplate.delete("http://localhost:9090/api/jobs/" + deleteId);
                        } catch (RestClientException e) {
                            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                    .body("Failed to delete job with id: " + deleteId + ". Error: " + e.getMessage());
                        }
                    }
                }
            } catch (RestClientException e) {
                if (!e.getMessage().contains("404")) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body("Error fetching jobs for company id: " + companyId + ". Error: " + e.getMessage());
                }
            }

            // Delete the company
            companyService.deleteById(companyId);
            return ResponseEntity.ok("Deleted company and its related data successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Company not found for id: " + id);
        }
    }

    @PostMapping("/getallsave")
    public ResponseEntity<String> saveCompanyJobAndReview(@RequestBody CompanyEntity companyEntity) {
        if (companyEntity != null && companyEntity.getId() > 0) {
            long companyId = companyEntity.getId();

            // Save company first
            CompanyEntity savedCompany = companyService.save(companyEntity);

            // Save jobs
            List<JobDTO> jobs = savedCompany.getJobs();
            if (jobs != null) {
                for (JobDTO job : jobs) {
                    job.setCompanyId(companyId); // Set company ID for each job
                    ResponseEntity<JobDTO> jobResponse = restTemplate.postForEntity(
                            "http://localhost:9090/api/jobs", job, JobDTO.class);
                    if (!jobResponse.getStatusCode().is2xxSuccessful()) {
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body("Failed to add job: " + job.getTitle());
                    }
                }
            }

            // Save reviews
            List<ReviewsDTO> reviews = companyEntity.getReviews();
            if (reviews != null) {
                for (ReviewsDTO review : reviews) {
                    review.setCompanyId(companyId); // Set company ID for each review
                    ResponseEntity<ReviewsDTO> reviewResponse = restTemplate.postForEntity(
                            "http://localhost:9090/api/reviews", review, ReviewsDTO.class);
                    if (!reviewResponse.getStatusCode().is2xxSuccessful()) {
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body("Failed to add review: " + review.getTitle());
                    }
                }
            }

            return ResponseEntity.ok("Company, jobs, and reviews saved successfully");
        } else {
            return ResponseEntity.badRequest().body("Invalid company data or Company ID is missing");
        }
    }


    
    
    
    @GetMapping("getreiewjobs/{id}")
    public ResponseEntity<?> getCompanyById(@PathVariable long id) {
        Optional<CompanyEntity> company = companyService.getCompanyById(id);
        if (company.isPresent()) {
            CompanyEntity companyEntity = company.get();

            // Fetch reviews data
            List<ReviewsDTO> reviews = null;
            try {
                ResponseEntity<ReviewsDTO[]> reviewsResponse = restTemplate.getForEntity(
                        "http://localhost:9090/api/reviews/byCompany/" + id,
                        ReviewsDTO[].class);

                if (reviewsResponse.getStatusCode().is2xxSuccessful()) {
                    reviews = Arrays.asList(reviewsResponse.getBody());
                }
            } catch (RestClientException e) {
                // Handle exception if needed
            }
            // Fetch jobs data
            List<JobDTO> jobs = null;
            try {
                ResponseEntity<JobDTO[]> jobsResponse = restTemplate.getForEntity(
                        "http://localhost:9090/api/jobs/byCompany/" + id,
                        JobDTO[].class);

                if (jobsResponse.getStatusCode().is2xxSuccessful()) {
                    jobs = Arrays.asList(jobsResponse.getBody());
                }
            } catch (RestClientException e) {
                // Handle exception if needed
            }

            // Set reviews and jobs to CompanyEntity
            companyEntity.setReviews(reviews);
            companyEntity.setJobs(jobs);

            return new ResponseEntity<>(companyEntity, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Company not found for get by id", HttpStatus.NOT_FOUND);
        }
    }
    
    @PostMapping
    public ResponseEntity<?> createCompany(@Valid @RequestBody CompanyEntity companyEntity) {
        CompanyEntity savedCompany = companyService.save(companyEntity);
        return new ResponseEntity<>(createResponse("Company created successfully", HttpStatus.CREATED, savedCompany), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCompany(@PathVariable long id, @Valid @RequestBody CompanyEntity companyEntity) {
        if (companyService.getCompanyById(id).isPresent()) {
            companyEntity.setId(id);
            CompanyEntity updatedCompany = companyService.save(companyEntity);
            return new ResponseEntity<>(createResponse("Company updated successfully", HttpStatus.OK, updatedCompany), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(createResponse("Company not found for update ", HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCompany(@PathVariable long id) {
        if (companyService.getCompanyById(id).isPresent()) {
            companyService.deleteById(id);
            return new ResponseEntity<>(createResponse("Company deleted successfully", HttpStatus.OK), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(createResponse("Company not found for delete ", HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
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