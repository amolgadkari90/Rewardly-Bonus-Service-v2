package com.rewardly.bonus.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;


@RestControllerAdvice
public class GlobalExceptionHandler {
	
	/**
	 *
	 */
	@ExceptionHandler(EmployeeNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleEmployeeNotFoundException(EmployeeNotFoundException ex, WebRequest request){
		ErrorResponse errorResponse = ErrorResponse.builder()
		.status(HttpStatus.NOT_FOUND.value())
		.errorCode(ex.getErrorCode())
		.errorMessage(ex.getMessage())
		.path(getRequestPath(request))
		.build();
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);		
	}
	
	/**
	 * Bonus calculation failure -  400/500 (Business logic error) 
	 */
	
	public ResponseEntity<ErrorResponse> handleBonusCalculationException(BonusCalculationException ex, WebRequest request){
		ErrorResponse errorResponse = ErrorResponse.builder()
				.success(false)								
				.status(HttpStatus.BAD_REQUEST.value())
				.errorCode(ex.getErrorCode())
				.errorMessage(ex.getMessage())
				.path(getRequestPath(request))
				.build();
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);		
	}
	
	private String getRequestPath(WebRequest request){
		return request.getDescription(false).replace("uri=", "");
	}
	/**
	 * Handles validation errors for @Valid annotated request bodies
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleValidationException(
			MethodArgumentNotValidException ex, 
			WebRequest request) {
		
		Map<String, String> errors = new HashMap<>();
		
		// Extract all field errors
		ex.getBindingResult().getAllErrors().forEach(error -> {
			String fieldName = ((FieldError) error).getField();
			String errorMessage = error.getDefaultMessage();
			errors.put(fieldName, errorMessage);
		});
		
		ErrorResponse errorResponse = ErrorResponse.builder()
				.success(false)
				.status(HttpStatus.BAD_REQUEST.value())
				.errorMessage("Validation failed")
				.errorCode("BAD_REQUEST")
				.errors(errors)
				.path(getRequestPath(request))
				.build();
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
	}
	
	
	//500
	@ExceptionHandler(GenricException.class)
	public ResponseEntity<ErrorResponse> handleGenricException(GenricException ex, WebRequest request){
		ErrorResponse errorResponse = ErrorResponse
											.builder()
											.success(false)
											.status(HttpStatus.INTERNAL_SERVER_ERROR.value())
											.errorCode(ex.getErrorCode())
											.errorMessage(ex.getMessage())
											.errors(null)
											.path(getRequestPath(request))
											.build();
		
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
	}
	
	@ExceptionHandler(CannotCreateTransactionException.class)
	public ResponseEntity<ErrorResponse> handleCannotCreateTransactionException(CannotCreateTransactionException ex,
			WebRequest request){
		ErrorResponse errorResponse = ErrorResponse.builder()
		.success(false)
		.status(HttpStatus.SERVICE_UNAVAILABLE.value())
		.errorCode("DB_CONNECTION_ERROR")
		.errorMessage("Database connection error. Please try again later")
		.path(getRequestPath(request))
		.build();
		return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(errorResponse);
	}	
}
