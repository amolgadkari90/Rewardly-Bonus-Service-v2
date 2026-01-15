package com.rewardly.bonus.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.rewardly.bonus.config.FeignConfig;
import com.rewardly.bonus.dto.EmployeeDto;

@FeignClient(name = "employee-service",
			configuration = FeignConfig.class,
			url = "${spring.cloud.openfeign.client.config.employee-service.url}"
			//fallback = EmployeeServiceFallback.class
			)
public interface EmployeeServiceClient {
	
	/**
     * Get employee by employeeId
     */
    @GetMapping("/api/v1/employees/{employeeId}")
    EmployeeDto getEmployeeById(
            @PathVariable("employeeId") String employeeId
    );

    /**
     * Get all employees
     */
    @GetMapping("/api/v1/employees")
    List<EmployeeDto> getAllEmployees();

    /**
     * Check if employee exists
     */
    @GetMapping("/api/v1/employees/{employeeId}/exists")
    Boolean checkEmployeeExists(
            @PathVariable("employeeId") String employeeId
    );
	

}
