package com.rewardly.bonus.enums;

public enum PerformanceBonusType {
	
	RATING_5(5, 0.10), // IF RATING = 5 THEN 10% BONUS
	RATING_4(4, 0.05), // IF RATING = 4 THEN 5% BONUS
	RATING_3_AND_BELOW(0, 0.00); // IF RATING <= 3 THEN 0% BONUS
	
	private final int rating;
	private final double percentage;
	
	PerformanceBonusType(int rating, double percentage){
		this.rating = rating;
		this.percentage = percentage;
	}
	
	public double getPercentage() {
		return percentage;
	}
	
	public static PerformanceBonusType from(int performanceRating) {
		if(performanceRating == 5) {
			return RATING_5;
		} 
		if(performanceRating == 4) {
			return RATING_4;
		}
		
		return RATING_3_AND_BELOW;
	}
}
