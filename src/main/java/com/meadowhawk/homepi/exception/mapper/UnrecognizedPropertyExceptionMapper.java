package com.meadowhawk.homepi.exception.mapper;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.codehaus.jackson.map.exc.UnrecognizedPropertyException;
import org.springframework.stereotype.Component;

import com.meadowhawk.homepi.exception.HomePiAppException;

/**
 * Returns a JSON Validation error when a service receives invalid or unparseable JSON content. 
 * @author Lee.Clarke
 */
@Provider
@Component
public class UnrecognizedPropertyExceptionMapper implements ExceptionMapper<UnrecognizedPropertyException>
{
    public Response toResponse(UnrecognizedPropertyException exception)
    {
    	String message = "Invalid JSON field: '"+exception.getUnrecognizedPropertyName()+"'";
        return Response
                .status(Status.BAD_REQUEST)
                .entity(new HomePiAppException(Status.BAD_REQUEST, message, "Invalid JSON, Unrecognized Property.",0).toResponse())
                .type( MediaType.APPLICATION_JSON)
                .build();
    }
}
