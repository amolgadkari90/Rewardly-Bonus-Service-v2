package com.rewardly.bonus.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.rewardly.bonus.entity.Bonus;

@Repository
public interface BonusRepository extends JpaRepository<Bonus, Long> {

	
	List<Bonus> findByEmployeeIdOrderByBonusDateDesc(String empId);
	
	@Transactional
	void deleteByEmployeeId(String empId);
	
	

}
