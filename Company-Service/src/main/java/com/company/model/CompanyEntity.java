package com.company.model;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Entity
@Data
public class CompanyEntity {
	
	   @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private long id;

	    @NotBlank(message = "Name is mandatory")
	    private String name;

	    @NotBlank(message = "Description is mandatory")
	    private String description;

	    @Transient
	    private List<JobDTO> jobs;

	    @Transient
	    private List<ReviewsDTO> reviews;
    

}
