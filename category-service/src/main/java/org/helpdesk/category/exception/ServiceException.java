package org.helpdesk.category.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.function.Supplier;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ServiceException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private static final String NOT_FOUND = "Category Id %s not found. Method %s.%s";

	public ServiceException(String message){
    	super(message);
    }

	public static Supplier<ServiceException> throwNotFound(String id){
		return ()->{
			ServiceException ex = new ServiceException(null);
			StackTraceElement cause = ex.getStackTrace()[2];
			return new ServiceException(String.format(NOT_FOUND,id,cause.getClassName(),cause.getMethodName()));
		};
	}
}
