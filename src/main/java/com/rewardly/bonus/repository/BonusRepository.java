package com.rewardly.bonus.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rewardly.bonus.entity.Bonus;

@Repository
public interface BonusRepository extends JpaRepository<Bonus, Long> {
	
	

}
