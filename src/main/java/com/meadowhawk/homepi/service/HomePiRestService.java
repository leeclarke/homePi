package com.meadowhawk.homepi.service;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.meadowhawk.homepi.model.LogEntries;
import com.meadowhawk.homepi.model.PiProfile;

@Path("/homepi")
@Produces(MediaType.APPLICATION_JSON)
public class HomePiRestService {

	@POST
	@Path("/pi/reg")
	public PiProfile registerPi() {
	
		return new PiProfile();
	}
	
	@GET
	@Path("/pi/{piSerialId}")
	public PiProfile getPiData(@PathParam("piSerialId") String piSerialId){
		
		return new PiProfile();
	}

	@POST
	@Path("/pi/{piSerialId}/log")
	public Response logStatus(@PathParam("piSerialId") String piSerialId){
		
		
		return Response.ok().build();
	}
	
	@GET
	@Path("/pi/{piSerialId}/log")
	public List<LogEntries> getLlogs(@PathParam("piSerialId") String piSerialId){
		
		
		return new ArrayList<LogEntries>();
	}
}