package com.meadowhawk.homepi.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import com.meadowhawk.homepi.exception.HomePiAppException;
import com.meadowhawk.homepi.model.LogEntries;
import com.meadowhawk.homepi.model.PiProfile;
import com.meadowhawk.homepi.service.business.ManagementService;

@Path("/homepi")
@Component
public class HomePiRestService {

	@Context UriInfo uriInfo;
	
	@Autowired
	ClassPathResource updateFile;
	
	@Value("${update.mainFile}")
	String mainUpdateFileName;
	
	@Value("${update.mainFileVersion}")
	Integer mainUpdateFileVersion;
	
	
	@Autowired
	@Qualifier("managementService")
	ManagementService managementService;
	
	@POST
	@Path("/pi/{piSerialId}/reg")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public PiProfile registerPi(@PathParam("piSerialId") String piSerialId, @Context HttpServletRequest request) {
		return managementService.createPiProfile(piSerialId, request.getRemoteAddr());
	}
	
	@GET
	@Path("/pi/{piSerialId}")
	@Produces(MediaType.APPLICATION_JSON)
	public PiProfile getPiData(@PathParam("piSerialId") String piSerialId) throws HomePiAppException{
		
		return managementService.getPiProfile(piSerialId);
	}

	@POST
	@Path("/pi/{piSerialId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response updatePiData(@PathParam("piSerialId") String piSerialId, PiProfile piProfile) throws HomePiAppException{
		if(piProfile == null){
			throw new HomePiAppException(Status.BAD_REQUEST, "No data provided in request.");
		}
//TODO: Make this into a Redirect!!!!!
		
		managementService.updatePiProfile(piProfile);
		return Response.ok(piProfile).build();  
	}
	
	@POST
	@Path("/pi/{piSerialId}/log")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response logStatus(@PathParam("piSerialId") String piSerialId){
		
		
		return Response.ok().build();
	}
	
	@GET
	@Path("/pi/{piSerialId}/log")
	@Produces(MediaType.APPLICATION_JSON)
	public List<LogEntries> getLlogs(@PathParam("piSerialId") String piSerialId){
		
		
		return new ArrayList<LogEntries>();
	}
	
	//TODO: add string replace on py file to update the version number
	@GET
	@Path("/update")
	@Produces("text/x-python")
	public Response getScriptUpdate(){
		File file;
		try {
			file = updateFile.getFile();
			ResponseBuilder response = Response.ok((Object) file);
			response.header("Content-Disposition", "attachment; filename=homePi.py");
			response.header("file-version", mainUpdateFileVersion);
			System.out.println("mainFile name=" + this.mainUpdateFileName);
			return response.build();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		return Response.noContent().build();
	}
}