package com.company.model;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReviewsDTO {
	
	private long id;
	
    private String title;

    
    private String description;

    
    private String rating;
    
  
    private Long companyId; // Ensure companyId is not null
    
  



}
