package org.helpdesk.posts.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class UnAuthorizationException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public UnAuthorizationException(String message){
    	super(message);
    }
}
