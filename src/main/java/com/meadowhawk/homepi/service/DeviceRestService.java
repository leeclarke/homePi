package com.meadowhawk.homepi.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
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

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import com.meadowhawk.homepi.exception.HomePiAppException;
import com.meadowhawk.homepi.model.LogData;
import com.meadowhawk.homepi.model.PiProfile;
import com.meadowhawk.homepi.service.business.HomePiUserService;
import com.meadowhawk.homepi.service.business.DeviceManagementService;
import com.meadowhawk.homepi.util.model.PublicRESTDoc;
import com.meadowhawk.homepi.util.model.PublicRESTDocMethod;
import com.meadowhawk.homepi.util.model.TODO;

@Path("/homepi/device")
@Component
@PublicRESTDoc(serviceName = "HomePi Device Service", description = "Pi focused management services specifically for Pis.")
public class DeviceRestService {
	private static Logger log = Logger.getLogger( DeviceRestService.class );
	private static final String API_KEY = "api_key";
	
	@Context UriInfo uriInfo;
	
	@Autowired
	ClassPathResource updateFile;
	
	@Value("${update.mainFile}")
	String mainUpdateFileName;
	
	@Value("${update.mainFileVersion}")
	Integer mainUpdateFileVersion;
	
	
	@Autowired
	DeviceManagementService deviceManagementService;
	
	@POST
	@Path("/pi/{piSerialId}/api")
	@Produces(MediaType.APPLICATION_JSON)
	@PublicRESTDocMethod(endPointName="Update Pi API Key", description="Updated the API key for the Pi. This can only be called by an auth user. Sadly for security reasons the user has to change the API stored on the PI manually. Returns 204 if sucessful.", sampleLinks={"/homepi/device/pi/01r735ds720/api"})
	public Response updatePiApiKey(@PathParam("piSerialId") String piSerialId, @HeaderParam(API_KEY) String apiKey) {
		deviceManagementService.updateApiKey(piSerialId,apiKey);
		return Response.noContent().build();
	}
	
	@POST
	@Path("/pi/{piSerialId}/reg")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@PublicRESTDocMethod(endPointName="Register Pi", description="Registers a new PI with the HomePi. This is called directly from the Pi, the first tiem the Pi runs the local HomePi application. The HomePi serial ID is mandatory and must match the Pi's actual number or things wont work out later on.. ", sampleLinks={"/homepi/device/pi/01r735ds720/reg"})
	public PiProfile registerPi(@PathParam("piSerialId") String piSerialId, @Context HttpServletRequest request) {
		return deviceManagementService.createPiProfile(piSerialId, request.getRemoteAddr());
	}
	
	@GET
	@Path("/pi/{piSerialId}")
	@Produces(MediaType.APPLICATION_JSON)
	@PublicRESTDocMethod(endPointName="Pi Profile", description="Returns registered info related to the given Pi serial id.", sampleLinks={"/homepi/device/pi/8lhdfenm1x"})
	public PiProfile getPiData(@PathParam("piSerialId") String piSerialId,@HeaderParam(API_KEY) String apiKey) throws HomePiAppException{
		return deviceManagementService.getPiProfile(piSerialId, apiKey);
	}	
	
	@POST
	@Path("/pi/{piSerialId}/log")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@PublicRESTDocMethod(endPointName="Log Pi Message", description="Logs a message from the Pi. Pi API key is required.", sampleLinks={"/homepi/device/pi/8lhdfenm1x/log"})
	public Response logStatus(@PathParam("piSerialId") String piSerialId){
		
		
		return Response.ok(new TODO()).build();
	}
	
	@GET
	@Path("/pi/{piSerialId}/log")
	@Produces(MediaType.APPLICATION_JSON)
	@PublicRESTDocMethod(endPointName="Log Pi Message", description="Retrieves logs entries for given Pi. Pi API key or user auth may be required.", sampleLinks={"/homepi/device/pi/8lhdfenm1x/log"})
	public Response getLlogs(@PathParam("piSerialId") String piSerialId){
		
		//Need to add params and privacy filtering on this.
		return Response.ok(new TODO()).build();
	}
	
	//TODO: add string replace on py file to update the version number
	//TODO: Feature :  add optional version number and allow Pi to provide its current version to prevent full download.
	//TODO: change path to /pi/{piSerialId}/update?curVer={X}
	@GET
	@Path("/pi/update")
	@Produces("text/x-python")
	@PublicRESTDocMethod(endPointName="Update Pi", description="EndPoint a Pi will call to request updates. Pi API key is required.", sampleLinks={"/homepi/device/pi/update"})
	public Response getScriptUpdate(){
		//TODO: Add API key verification
		File file;
		try {
			file = updateFile.getFile();
			ResponseBuilder response = Response.ok((Object) file);
			response.header("Content-Disposition", "attachment; filename=homePi.py");
			response.header("file-version", mainUpdateFileVersion);
			log.debug("mainFile name=" + this.mainUpdateFileName);
			return response.build();
		} catch (IOException e) {
			log.warn("Error while trying to get update file.", e);
		}
		 
		return Response.noContent().build();
	}
}