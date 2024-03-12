package com.example.lab6.EmployeeManagementSystem.Model;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Employee {

    @NotEmpty(message = "Id cannot be empty")
    @Size(min = 2, message = "Id must be at least 2 characters")
    private String id;

    @NotEmpty(message = "Name cannot be null")
    @Size(min = 4, message = "Name must be at least 4 characters")
    @Pattern(regexp = "^[a-zA-Z ]+$", message = "Name must contain only characters (no numbers)")
    private String name;

    @Email(regexp = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$",
    message = "Invalid email format")
    private String email;

    @Pattern(regexp = "^05\\d{8}$", message = "Phone number must start with 05 and consist of exactly 10 digits")
    private String phoneNumber;

    @NotNull(message = "Age cannot be empty")
    @Min(value = 25, message = "Age must be at least 25")
    private Integer age;

    @NotEmpty(message = "Position cannot be empty")
    @Pattern(regexp = "^(Supervisor|Coordinator)$",
            message = "Position must be either 'Supervisor' or 'Coordinator' only")
    private String position;

    private boolean onLeave = false;

    @NotNull(message = "Hire date cannot be empty")
    @PastOrPresent(message = "Hire date must be in the past or present")
    private LocalDate hireDate;

    @NotNull(message = "Annual leave cannot be empty")
    @Positive(message = "Annual leave must be a positive number")
    private Integer annualLeave;
}
