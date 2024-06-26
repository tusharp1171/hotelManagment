package com.company.model;	
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
@Data
@Getter
@Setter
public class JobDTO  {

    private long id;
    private String title;
    private String description;
    private float minSalary;
    private float maxSalary;
    private String location;
    private Long companyId; // Ensure companyId is not null
}
