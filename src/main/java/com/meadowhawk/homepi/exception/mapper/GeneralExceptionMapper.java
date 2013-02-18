package com.meadowhawk.homepi.exception.mapper;


import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.exc.UnrecognizedPropertyException;
import org.springframework.stereotype.Component;

import com.meadowhawk.homepi.exception.HomePiAppException;

/**
 * General Exception mapper for catching all exceptions not mapping to an expected Exception.
 * @author Lee.Clarke
 */
@Provider
@Component
public class GeneralExceptionMapper implements ExceptionMapper<Exception>{
	private Log log = LogFactory.getLog(GeneralExceptionMapper.class);
	
	public Response toResponse(Exception exception) {
		String msg = "General Exception at CXF layer:" +exception.toString();
		log.debug(msg,exception);
		if(exception instanceof NullPointerException){
			return new NullPointerExceptionMapper().toResponse((NullPointerException)exception);
		} else if(exception instanceof HomePiAppException){
			return new HomePiAppExceptionMapper().toResponse((HomePiAppException)exception);
		} else if(exception instanceof JsonProcessingException){
			return new JsonProcessingExceptionMapper().toResponse((JsonProcessingException)exception);
		} else if(exception instanceof UnrecognizedPropertyException){
			return new UnrecognizedPropertyExceptionMapper().toResponse((UnrecognizedPropertyException)exception);
		} else if(exception instanceof WebApplicationException){
			WebApplicationException wae = (WebApplicationException)exception;
			Status status = Status.fromStatusCode(wae.getResponse().getStatus());
			String cause = (wae.getCause()!= null)?wae.getCause().toString():"";
			if(status == null && wae.getResponse().getStatus() == 405){
				status = Status.NOT_ACCEPTABLE;
				msg += (" [Unmapped Status:" + wae.getResponse().getStatus()+"]");
				if(cause == null) cause = "Method Not Allowed";
			}
			return Response
				.status(status)
                .entity(new HomePiAppException(status, msg, cause,0).toResponse())
                .type(MediaType.APPLICATION_JSON)
                .build();
		}
		
		return Response
			.status(Status.BAD_REQUEST)
            .entity(new HomePiAppException(Status.BAD_REQUEST, msg, "",0).toResponse())
            .type(MediaType.APPLICATION_JSON)
            .build();
	}

}
