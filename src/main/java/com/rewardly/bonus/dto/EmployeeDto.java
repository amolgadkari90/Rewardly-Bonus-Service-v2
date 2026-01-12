package com.rewardly.bonus.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDto {
	
	private String empId;
	private String empName;
	private BigDecimal empSalary;
	private String empDesignation;
	private Integer empPerformanceRating;
	private BigDecimal empExperienceYears;	

}
