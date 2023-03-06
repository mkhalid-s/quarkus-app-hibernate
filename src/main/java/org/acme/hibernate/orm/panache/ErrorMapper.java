package org.acme.hibernate.orm.panache;

import javax.ws.rs.ext.Provider;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import io.smallrye.mutiny.CompositeException;
import lombok.extern.log4j.Log4j2;

import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

/**
 * 
 */
@Provider
@Log4j2
public class ErrorMapper implements ExceptionMapper<Exception> {

	@Inject
	ObjectMapper objectMapper;

	@Override
	public Response toResponse(Exception exception) {
		log.error("Failed to handle request", exception);
		Throwable throwable = exception;

		// Default to HTTP 500
		int code = 500;
		if (throwable instanceof WebApplicationException) {
			code = ((WebApplicationException) exception).getResponse().getStatus();
		}

		// This is a Mutiny exception and it happens, for example, when we try to insert
		// a new
		// fruit but the name is already in the database
		if (throwable instanceof CompositeException) {
			throwable = throwable.getCause();
		}

		ObjectNode exceptionJson = objectMapper.createObjectNode();
		exceptionJson.put("exceptionType", throwable.getClass().getName());
		exceptionJson.put("code", code);

		if (exception.getMessage() != null) {
			exceptionJson.put("error", throwable.getMessage());
		}

		return Response.status(code).entity(exceptionJson).build();
	}

}
