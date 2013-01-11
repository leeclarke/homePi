package com.meadowhawk.homepi.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import com.meadowhawk.homepi.model.LogEntries;
import com.meadowhawk.homepi.model.PiProfile;
import com.meadowhawk.homepi.service.business.ManagementService;

@Path("/homepi")
@Component
@Produces(MediaType.APPLICATION_JSON)
public class HomePiRestService {

	@Autowired
	ClassPathResource updateFile;
	
	@Autowired
	@Qualifier("managementService")
	ManagementService managementService;
	
	@POST
	@Path("/pi/reg")
	public PiProfile registerPi() {
	
		return new PiProfile();
	}
	
	@GET
	@Path("/pi/{piSerialId}")
	public PiProfile getPiData(@PathParam("piSerialId") String piSerialId){
		
		return managementService.getPiProfile(piSerialId);
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
	
	@GET
	@Path("/update")
	@Produces("text/x-python")
	public Response getScriptUpdate(){
		 File file;
		try {
			file = updateFile.getFile();
			ResponseBuilder response = Response.ok((Object) file);
			response.header("Content-Disposition",
					"attachment; filename=hello.py");
			return response.build();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		return Response.noContent().build();
	}
}