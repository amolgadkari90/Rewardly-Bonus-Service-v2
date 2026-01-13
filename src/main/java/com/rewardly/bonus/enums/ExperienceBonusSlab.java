package com.rewardly.bonus.enums;

public enum ExperienceBonusSlab {
	
	LESS_THAN_3(0, 2, 0.05), //EXPERIENCE < 3 THEN 5% BONUS
	BETWEEN_3_AND_5(3, 5, 0.10), // EXPERIENCE BETWEEN 3 AND 5 THEN 10% BONUS
	GREATER_THAN_5(6, Integer.MAX_VALUE, 0.20); //EXPERIENCE > 5 THEN 20% BONUS
	
	private final int minYears;
	private final int maxYears;
	private final double percentage;
	
	ExperienceBonusSlab(int minYears, int maxYears, double percentage){
		this.minYears = minYears;
		this.maxYears = maxYears;
		this.percentage = percentage;
	}
	
	public double getPercentage() {
		return percentage;
	}
	
	public static ExperienceBonusSlab fromExperience(int experienceYears) {
		
		for(ExperienceBonusSlab slab : values()) {
			if(experienceYears >= slab.minYears && experienceYears <= slab.maxYears) {
				return slab;
			}			
		}
		
		throw new IllegalArgumentException("Invalid experience years: "+ experienceYears);		
	}
	

}
