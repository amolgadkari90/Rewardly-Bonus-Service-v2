package com.rewardly.bonus.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BonusApiResponse <T> {
	
	private Boolean success;
	private T data;
	private String message;
	private String path;
	private Integer statusCode;
	@Builder.Default
	@JsonFormat(pattern =  "yyyy-MM-dd'T'HH:mm:ss")
	private LocalDateTime timeStamp = LocalDateTime.now();	

}
