package com.rewardly.bonus.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class BonusDto {
	
	private Long bonusId;
	private String empId;
	private String empName;
	private BigDecimal experienceBonus;
	private BigDecimal designationBonus;
	private BigDecimal performanceBonus;
	private BigDecimal totalBonusBeforeTax;
	private BigDecimal taxDeducted;
	private BigDecimal taxRateApplied;
	private BigDecimal netBonus;
	private BigDecimal bonusPercentage;
	private LocalDate bonusDate;
	private Integer experienceYearsAtCalculation;	

}
