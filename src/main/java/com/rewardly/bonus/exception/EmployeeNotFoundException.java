package com.rewardly.bonus.exception;


public class EmployeeNotFoundException extends BaseException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7153173678911981127L;
	
	//404
	private static final String ERROR_CODE="EMPLOYEE_NOT_FOUND";
	public EmployeeNotFoundException(String id) {
		super(
				String.format("Employee not found with ID: %s",id),
						ERROR_CODE, id);
	}

}
