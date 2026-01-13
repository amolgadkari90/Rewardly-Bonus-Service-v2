package com.rewardly.bonus.enums;

import java.util.Arrays;

public enum DesignationBonusType {
	
	MANAGER("manager", 0.05), // FOR MANAGER 5% BONUS
	EXECUTIVE ("executive", 0.02); // FOR EXECUTIVE 2% BONUS
	
	private final String designation;
	private final double percentage;
	
	DesignationBonusType(String designation, double percentage){
		this.designation = designation;
		this.percentage = percentage;
	}
	
	public double getPercentage() {
		return percentage;
	}
	
	public static DesignationBonusType from(String designation ) {
		//Enum values applied stream -> 
		//applying filter on designation supplied -> 
		//Finding first match value 
		//Then returns designation enum constant object
		//OrElse throws exception for invalid designation 
				
		return Arrays.stream(values())
					.filter(type -> type.designation.equalsIgnoreCase(designation))
					.findFirst()
					.orElseThrow(() -> new IllegalArgumentException("Invalid designation: " + designation)
					);		
	}

}
