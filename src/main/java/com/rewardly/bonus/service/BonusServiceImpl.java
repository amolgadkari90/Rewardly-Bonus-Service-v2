package com.rewardly.bonus.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rewardly.bonus.client.EmployeeServiceClient;
import com.rewardly.bonus.dto.BonusDto;
import com.rewardly.bonus.dto.EmployeeDto;
import com.rewardly.bonus.entity.Bonus;
import com.rewardly.bonus.enums.DesignationBonusType;
import com.rewardly.bonus.enums.ExperienceBonusSlab;
import com.rewardly.bonus.enums.PerformanceBonusType;
import com.rewardly.bonus.exception.BonusCalculationException;
import com.rewardly.bonus.exception.EmployeeNotFoundException;
import com.rewardly.bonus.mapper.BonusMapper;
import com.rewardly.bonus.repository.BonusRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor 

public class BonusServiceImpl implements BonusService {
	//Constants 
	private static final BigDecimal ONE_HUNDRED = BigDecimal.valueOf(100);	
	private static final BigDecimal TWO_LAKH = BigDecimal.valueOf(200_000);
	private static final BigDecimal ONE_LAKH = BigDecimal.valueOf(100_000);
	
	//Compiler level dependency injection
	private final BonusRepository bonusRepository;
	private final BonusMapper bonusMapper;
	
	// Field level dependency injection
	@Autowired 
	private EmployeeServiceClient employeeClient;
	
		
	@Override
	public BonusDto calculateAndSaveBonus(String empId) {
		/*
		 * 1. Check if empId is valid? No: throw BonusCalculationException
		 * 2. Declare EmployeeDto
		 * 3. Fetch employee using Feign client 
		 * 4. Assign fetched employee to employeeDto
		 * 5. If employee not found through Feign Client then throw EmpployeeNotFound exception
		 * 6. If employeeDto == NULL -> Throw EmployeeNotFound exception
		 * 7. If Salary < 0 || NULL -> throw BonusCalculationException
		 * 8. Perform Bonus Calculation
		 * 9. Build Bonus object
		 * 10. Save bonus to repository 
		 * 11. Map savedBonus to bonusDto object -> + Add empName to bonusDto
		 * 12. Return bonusDto
		 * */
		log.info("Starting bonus calculations for empId= {} ", empId);
		
		if(empId == null || empId.isBlank()) {
			log.error("Employee id is null/blank = {}",empId);
			throw new BonusCalculationException("Employee id cann't be null or empty!...");
		}
		EmployeeDto employeeDto = fetchEmployee(empId);	
		//Salary
		BigDecimal empSalary = employeeDto.getEmpSalary();
		if(empSalary == null || 
				empSalary.compareTo(BigDecimal.ZERO) <= 0) {
			log.error("Invalid employee salary for empId = {}", empId);
			throw new BonusCalculationException("Invalid employee salary!...");
		}	

		//Experience 
		int empExperienceYears = employeeDto.getEmpExperienceYears().intValue();		
		// Designation 
		String empDesignation = employeeDto.getEmpDesignation();
		//Performance 
		Integer empPerformanceRating = employeeDto.getEmpPerformanceRating();
		
		log.debug("Employee data fetched | salary = {} | Experience = {} | Designation = {} | Rating = {}"
				,empSalary, empExperienceYears, empDesignation, empPerformanceRating );
		
		//Calculation of experienceBonus
		BigDecimal experienceBonus = calculateExperienceBonus(empSalary, empExperienceYears);
		log.debug("Experience bonus calculated: {}",experienceBonus);
		
		//Calculation of Designation bonus		
		BigDecimal designationBonus = calculateDesignationBonus(empSalary, empDesignation);
		log.debug("Designation bonus calculated: {}",designationBonus);
		
		//Calculation of Performance bonus		
		BigDecimal perormanceBonus = calculatePerformanceBonus(empSalary, empPerformanceRating);
		log.debug("Perormance bonus calculated: {}",perormanceBonus);
		
		//Calculate totalBonusBeforeTax
		BigDecimal totalBonusBeforeTax = 
						calculateTotalBonusBeforeTax(experienceBonus, designationBonus, perormanceBonus );
		log.info("Total bonus before tax calculated: {}",totalBonusBeforeTax);
		//Calculate tax amount 
		
		BigDecimal taxAmount =  calculateTaxAmount(empSalary, totalBonusBeforeTax);
		log.info("Tax amount calculated: {}",taxAmount);
		
		//Calculate net bonus
		BigDecimal netBonus = calculateNetBonus(totalBonusBeforeTax, taxAmount);
		log.info("Net bonus calculated: {}",netBonus);
		
		//Calculate bonus percentage
		BigDecimal bonusPercentage =  calculateBonusPercentage(empSalary, totalBonusBeforeTax );
		log.debug("Bonus percentage calculated: {}%",bonusPercentage);
		//Build Bonus entity/object
		
		Bonus bonus = Bonus.builder()
							.employeeId(empId)
							.experienceBonus(experienceBonus)
							.designationBonus(designationBonus)
							.performanceBonus(perormanceBonus)
							.totalBonusBeforeTax(totalBonusBeforeTax)
							.taxDeducted(taxAmount)
							.netBonus(netBonus)
							.bonusPercentage(bonusPercentage)
							.bonusDate(LocalDate.now())
							.build();
		
		//Save bonus object to DB		
		Bonus savedBonus = bonusRepository.save(bonus);
		log.info("Bonus saved successfully | bonusId = {} | empId = {} ", savedBonus.getBonusId(), savedBonus.getEmployeeId());
		
		//Map savedBonus to bonusDto object -> + Add empName to bonusDto
		
		BonusDto bonusDto = bonusMapper.toDto(savedBonus);
		bonusDto.setEmpName(employeeDto.getEmpName());
		
		return bonusDto; // Returning bonusDto
	}

	private EmployeeDto fetchEmployee(String empId) {
		log.debug("Fetching employee details for empId = {}",empId);
		try {
			EmployeeDto employee = employeeClient.getEmployeeById(empId);
			if(employee == null ) {
				log.error("Employee not found for empId: {}", empId);
				throw new EmployeeNotFoundException(empId);
			}
		return employee;			
		} catch(Exception ex) {
			log.error("Error fetching employee details for empId = {}",empId, ex);
			throw new EmployeeNotFoundException(empId);
		}		
	}

	private BigDecimal calculateExperienceBonus(BigDecimal empSalary, int empExperienceYears) {
		
		ExperienceBonusSlab slabExperience = ExperienceBonusSlab.fromExperience(empExperienceYears);	
		log.debug("Experience slab selected: {} ({}%)",slabExperience.name(), slabExperience.getPercentage());
		return empSalary.multiply(BigDecimal.valueOf(slabExperience.getPercentage()));
	}
	
	private BigDecimal calculateDesignationBonus(BigDecimal empSalary, String empDesignation) {
		DesignationBonusType slabDesignation = DesignationBonusType.from(empDesignation);	
		log.debug("Designation bonus type selected: {} ({}%)",slabDesignation.name(), slabDesignation.getPercentage());
		return empSalary.multiply(BigDecimal.valueOf(slabDesignation.getPercentage()));
	}
	
	private BigDecimal calculatePerformanceBonus(BigDecimal empSalary, Integer empPerformanceRating) {
		PerformanceBonusType slabRating = PerformanceBonusType.from(empPerformanceRating);
		log.debug("Performance bonus type selected: {} ({}%)",slabRating.name(), slabRating.getPercentage());
		return empSalary.multiply(BigDecimal.valueOf(slabRating.getPercentage()));
	}
	
	private BigDecimal calculateTotalBonusBeforeTax(BigDecimal experienceBonus, BigDecimal designationBonus,
			BigDecimal perormanceBonus) {		
		//BigDecimal.ZERO -> for NULL safety,
		//BigDecimal.setScale(2, RoundingMode.HALF_UP) -> Round of 2 digits after decimal 
		//(HALF_UP) -> rounding to the nearest neighbour -> (Example 10.234 = 10.23, 10.235 = 10.24, 49.9995 =  50.00)
		
		return BigDecimal.ZERO.add(experienceBonus)
							 .add(designationBonus)
							 .add(perormanceBonus)
							 .setScale(2, RoundingMode.HALF_UP);
	}
	
	
	private BigDecimal calculateTaxAmount(BigDecimal empSalary, BigDecimal totalBonusBeforeTax) {
		BigDecimal salaryPlusBonus = empSalary.add(totalBonusBeforeTax);
		
		BigDecimal taxRate = determineTaxRate(salaryPlusBonus);
		
		log.debug("Tax rate determined: {}%", taxRate);
		//Return totalBonusBeforeTax * (taxRate/100) for double data type		
		return totalBonusBeforeTax.multiply(taxRate)
								.divide(ONE_HUNDRED)
								.setScale(2, RoundingMode.HALF_UP);
	}

	private BigDecimal determineTaxRate(BigDecimal salaryPlusBonus) {
		
		if(salaryPlusBonus.compareTo(TWO_LAKH) > 0) {
			//If (salaryPlusBonus > 200_000) -> for double datatype 
			//return 20.0; 
			return BigDecimal.valueOf(20); // Return 20% tax deduction 			
		}
		
		if(salaryPlusBonus.compareTo(ONE_LAKH) > 0) {
			//If (salaryPlusBonus > 100_000) -> for double data type 
			//return 15.0; 
			return BigDecimal.valueOf(15);  // Return 15% tax deduction 		
		}
		
		return BigDecimal.ZERO; // Else if < 100_000 return 0.0 tax deduction 
	}
	
	private BigDecimal calculateNetBonus(BigDecimal totalBonusBeforeTax, BigDecimal taxAmount) {
		return totalBonusBeforeTax.subtract(taxAmount);
	}
	

	private BigDecimal calculateBonusPercentage(BigDecimal empSalary, BigDecimal totalBonusBeforeTax) {
		
		//If salary <= 0 then return 0
		//else return (totalBonusBeforeTax/empSalary)*100
		if(empSalary.compareTo(BigDecimal.ZERO) <= 0 ) {
			return BigDecimal.ZERO;
		}
		//divide(empSalary, 4, RoundingMode.HALF_UP) -> (3.1416 [4 digits after decimal point])
		// 2.35%
		return totalBonusBeforeTax.divide(empSalary, 4, RoundingMode.HALF_UP)
								.multiply(ONE_HUNDRED)
								.setScale(2, RoundingMode.HALF_UP);
	}


	@Override
	@Transactional(readOnly = true)
	public List<BonusDto> getBonusByEmpId(String empId) {
		
		log.info("Fetching bonuses for empId: {}", empId);		
		List<Bonus> bonuses = bonusRepository.findByEmployeeIdOrderByBonusDateDesc(empId);
		List<BonusDto> listBonuses = bonuses.stream().map(bonusMapper::toDto).collect(Collectors.toList());
		log.info("Fetched {} bonuses for empId = {}",listBonuses.size(), empId );
		return listBonuses;
	}

	@Override
	public void deleteBonusByEmpId(String empId) {
		log.info("Deleting all bonuses for the empId: {}", empId);
		bonusRepository.deleteByEmployeeId(empId);	
		log.info("All bonuses deleted for empId = {}",empId);
	}

	@Override
	@Transactional(readOnly = true)
	public List<BonusDto> getAllBonuses(Pageable pageable) {
		
		log.info("Fetching all bonuses | Page = {}, size = {} ", 
				pageable.getPageNumber(), pageable.getPageSize());
		Page<Bonus> bonusPage = bonusRepository.findAll(pageable);
		List<BonusDto> bonusList = bonusPage.stream().map(bonusMapper::toDto).collect(Collectors.toList());
		log.info("Fetched {} bonuses in current page ", bonusList.size());		
		return bonusList;
	}

}
