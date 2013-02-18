package com.meadowhawk.homepi.exception.mapper;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import com.meadowhawk.homepi.exception.HomePiAppException;

@Provider
@Component
public class NullPointerExceptionMapper implements ExceptionMapper<NullPointerException>{
	private Log log = LogFactory.getLog(NullPointerExceptionMapper.class);
	
	public Response toResponse(NullPointerException exception) {
		String message = "Invalid JSON input: request is missing data. Error: NullValue";
		log.warn(message ,exception);
        return Response
                .status(Response.Status.BAD_REQUEST)
                .entity(new HomePiAppException(Status.BAD_REQUEST, message, exception.getCause().toString(),0).toResponse())
                .type( MediaType.APPLICATION_JSON)
                .build();
	}

}