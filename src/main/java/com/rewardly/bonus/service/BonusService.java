package com.rewardly.bonus.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.rewardly.bonus.dto.BonusDto;

public interface BonusService {
	
	BonusDto calculateAndSaveBonus(String empId);
	
	List<BonusDto> getBonusByEmpId(String empId);
	
	void deleteBonusByEmpId(String empId);
	
	List<BonusDto> getAllBonuses(Pageable pageable);
}
