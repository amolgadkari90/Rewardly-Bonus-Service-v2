package com.rewardly.bonus.exception;

import lombok.Getter;

@Getter
public abstract class BaseException extends RuntimeException {	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5372140435160502006L;
	private final String errorCode;
	private final Object[] args;
	

	public BaseException(String message,String errorCode, Throwable cause) {
		super(message, cause);
		this.errorCode=errorCode;
		this.args=null;
	}
	public BaseException(String message,String errorCode) {
		super(message);
		this.errorCode=errorCode;
		this.args=null;
	}
	public BaseException(String message,String errorCode,Object... args) {
		super(message);
		this.errorCode=errorCode;
		this.args=args;
	}
}
