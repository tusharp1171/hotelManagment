package com.jobService.model;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Entity
@Data
public class JobEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "Title is mandatory")
    private String title;

    @NotBlank(message = "Description is mandatory")
    private String description;

    @PositiveOrZero(message = "Minimum salary must be zero or positive")
    private float minSalary;

    @PositiveOrZero(message = "Maximum salary must be zero or positive")
    private float maxSalary;

    @NotBlank(message = "Location is mandatory")
    private String location;

    @NotNull(message = "Company ID is mandatory")
    private long companyid;
}