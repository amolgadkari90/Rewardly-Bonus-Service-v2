package com.rewardly.bonus.exception;

public class GenricException extends BaseException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5886115281012908587L;

	// 500
	private static final String ERROR_CODE = "INTERNAL_ERROR";

	public GenricException(String id) {
			super(
					String.format("Employee not found with ID: %s",id),
							ERROR_CODE, id);
		}
}
