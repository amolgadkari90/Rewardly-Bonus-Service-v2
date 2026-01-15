package com.rewardly.bonus.client;

import java.util.List;

import org.springframework.stereotype.Component;

import com.rewardly.bonus.dto.EmployeeDto;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class EmployeeServiceFallback implements EmployeeServiceClient{@Override
	
	public EmployeeDto getEmployeeById(String employeeId) {
		// TODO Auto-generated method stub
		return null;
	}

@Override
public List<EmployeeDto> getAllEmployees() {
	// TODO Auto-generated method stub
	return null;
}

@Override
public Boolean checkEmployeeExists(String employeeId) {
	// TODO Auto-generated method stub
	return null;
}	

}
