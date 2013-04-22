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
import com.meadowhawk.homepi.util.model.PublicRESTDoc;
import com.meadowhawk.homepi.util.model.PublicRESTDocMethod;

@Path("/homepi")
@Component
@PublicRESTDoc(serviceName = "HomePiService", description = "Pi focused management services.")
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
	@PublicRESTDocMethod(endPointName="Register Pi", description="Registers a new PI with the HomePi. This is called directly from the Pi, the first tiem the Pi runs the local HomePi application. The HomePi serial ID is mandatory and must match the Pi's actual number or things wont work out later on.. ", sampleLinks={"/homepi/pi/01r735ds720/reg"})
	public PiProfile registerPi(@PathParam("piSerialId") String piSerialId, @Context HttpServletRequest request) {
		return managementService.createPiProfile(piSerialId, request.getRemoteAddr());
	}
	
	@POST
	@Path("/pi/{piSerialId}/api/{apiKey}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@PublicRESTDocMethod(endPointName="Update Pi API Key", description="Updated the API key for the Pi. This can only be called by an auth user. Sadly for security reasons the user has to change the API stored on the PI manually.", sampleLinks={"/homepi/pi/01r735ds720/reg/api/de4d9e75-d6b3-43d7-9fef-3fb958356ded"})
	public PiProfile updatePiApiKey(@PathParam("piSerialId") String piSerialId, @Context String apiKey) {
		//TODO: Add user auth here!
		return managementService.updateApiKey(piSerialId, apiKey);
	}
	
	@GET
	@Path("/pi/{piSerialId}")
	@Produces(MediaType.APPLICATION_JSON)
	@PublicRESTDocMethod(endPointName="Pi Profile", description="Returns registered info related to the given Pi serial id.", sampleLinks={"/homepi/pi/01r735ds720"})
	public PiProfile getPiData(@PathParam("piSerialId") String piSerialId) throws HomePiAppException{
		
		return managementService.getPiProfile(piSerialId);
	}

	@POST
	@Path("/pi/{piSerialId}")
	@Produces(MediaType.APPLICATION_JSON)
	@PublicRESTDocMethod(endPointName="Update Pi Profile", description="Updates a Pi profile, this requires a valid user auth or the Pi API key.", sampleLinks={"/homepi/pi/01r735ds720"})
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
	@PublicRESTDocMethod(endPointName="Log Pi Message", description="Logs a message from the Pi. Pi API key is required.", sampleLinks={"/homepi/pi/01r735ds720/log"})
	public Response logStatus(@PathParam("piSerialId") String piSerialId){
		
		
		return Response.ok().build();
	}
	
	@GET
	@Path("/pi/{piSerialId}/log")
	@Produces(MediaType.APPLICATION_JSON)
	@PublicRESTDocMethod(endPointName="Log Pi Message", description="Retrieves logs entries for given Pi. Pi API key or user auth may be required.", sampleLinks={"/homepi/pi/01r735ds720/log"})
	public List<LogEntries> getLlogs(@PathParam("piSerialId") String piSerialId){
		
		//Need to add params and privacy filtering on this.
		return new ArrayList<LogEntries>();
	}
	
	//TODO: add string replace on py file to update the version number
	@GET
	@Path("/update")
	@Produces("text/x-python")
	@PublicRESTDocMethod(endPointName="Update Pi", description="EndPoint a Pi will call to request updates. Pi API key is required.", sampleLinks={"/homepi/pi/update"})
	public Response getScriptUpdate(){
		//TODO: Add API key verification
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