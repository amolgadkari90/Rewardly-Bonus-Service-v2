package com.rewardly.bonus.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "bonus")
public class Bonus {
	
	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	@Column (name = "bonus_id")
	private Long bonusId;
	
	@Column (name = "employee_id", nullable = false)
	private String employeeId;
	
	@Column (name = "experience_years_at_calculation")
	private Integer experienceYearsAtCalculation;
	
	@Column (name = "experience_bonus")
	private BigDecimal experienceBonus;
	
	@Column (name = "designation_bonus")
	private BigDecimal designationBonus;
	
	@Column (name = "performance_bonus")
	private BigDecimal performanceBonus;
	
	@Column (name = "total_bonus_before_tax")
	private BigDecimal totalBonusBeforeTax;
	
	@Column (name = "tax_deducted" , nullable = false)
	private BigDecimal taxDeducted;
	
	@Column (name = "tax_rate_applied")
	private BigDecimal taxRateApplied;
	
	@Column (name = "net_bonus")
	private BigDecimal netBonus;
	
	@Column (name = "bonus_amount" , nullable = false)
	private BigDecimal bonusAmount;
	
	@Column (name = "bonus_percentage")
	private BigDecimal bonusPercentage;
	
	@Column (name = "bonus_date", nullable = false)
	private LocalDate bonusDate;	 

}
