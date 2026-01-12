package com.rewardly.bonus.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class BonusRequestDto {
	@NotNull (message = "Employee id is required!...")
	@Pattern(regexp = "^rewardlyEmp-\\d{8}-\\d{6}-\\d{4}$", 
			 message = "Employee ID must follow format: rewardlyEmp-YYYYMMDD-HHMMSS-####")
	private String employeeId;
	
	@PastOrPresent(message ="Bonuses can't be in future!...")
	private LocalDate bonusDate;	

}
