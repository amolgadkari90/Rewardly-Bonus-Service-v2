package com.rewardly.bonus.exception;

public class BonusCalculationException extends BaseException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2738277119201595522L;
	
	private static final String ERROR_CODE="BONUS_CALCULATION_FAILED";
	public BonusCalculationException(String reason) {
		super(String.format("Bonus calculation failed: %s ", reason), ERROR_CODE, reason);		
	}
	
	public BonusCalculationException(String reason , Throwable cause) {
		super(String.format("Bonus calculation failed: %s ", reason), ERROR_CODE, cause);		
	}
}
