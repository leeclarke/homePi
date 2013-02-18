package com.meadowhawk.homepi.exception.mapper;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.springframework.stereotype.Component;

import com.meadowhawk.homepi.exception.HomePiAppException;

@Provider
@Component
public class HomePiAppExceptionMapper implements ExceptionMapper<HomePiAppException> {

	public Response toResponse(HomePiAppException e) {
		return Response
                .status((e.getStatus() != null)? e.getStatus():Response.Status.BAD_REQUEST)
                .entity(e.toResponse())
                .type(MediaType.APPLICATION_JSON)
                .build();
	}

}
