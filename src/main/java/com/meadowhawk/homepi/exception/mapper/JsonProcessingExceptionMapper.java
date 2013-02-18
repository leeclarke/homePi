package com.meadowhawk.homepi.exception.mapper;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.codehaus.jackson.JsonProcessingException;
import org.springframework.stereotype.Component;

import com.meadowhawk.homepi.exception.HomePiAppException;

/**
 * Global Parser Exception which will map all JSON parsing errors to a 400 error and provide feedback on error.
 * @author Lee.Clarke
 */
@Provider
@Component
public class JsonProcessingExceptionMapper implements ExceptionMapper<JsonProcessingException>{

	public Response toResponse(JsonProcessingException exception) {
		String message = "Invalid JSON: '"+exception.getMessage();
        return Response
                .status(Status.BAD_REQUEST)
                .entity(new HomePiAppException(Status.BAD_REQUEST, message, "Invalid JSON",0).toResponse())
                .type( MediaType.APPLICATION_JSON)
                .build();
    }

}
