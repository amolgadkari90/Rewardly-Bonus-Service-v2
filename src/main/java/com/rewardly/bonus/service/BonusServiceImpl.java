package com.rewardly.bonus.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rewardly.bonus.client.EmployeeServiceClient;
import com.rewardly.bonus.dto.BonusDto;
import com.rewardly.bonus.mapper.BonusMapper;
import com.rewardly.bonus.repository.BonusRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor 

public class BonusServiceImpl implements BonusService {
	
	//Compiler level dependency injection
	private final BonusRepository bonusRepository;
	private final BonusMapper bonusMapper;
	
	// Field level dependency injection
	@Autowired 
	private EmployeeServiceClient employeeClient;
	
		
	@Override
	public BonusDto calculateAndSaveBonus(String empId) {
		// TODO Auto-generated method stub		
		return null;
	}

	@Override
	public List<BonusDto> getBonusByEmpId(String empId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteBonusByEmpId(String empId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<BonusDto> getAllBonuses(Pageable pageable) {
		// TODO Auto-generated method stub
		return null;
	}

}
