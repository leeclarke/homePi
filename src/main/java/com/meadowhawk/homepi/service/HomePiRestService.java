package com.meadowhawk.homepi.service;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

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
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import com.meadowhawk.homepi.exception.HomePiAppException;
import com.meadowhawk.homepi.model.HomePiUser;
import com.meadowhawk.homepi.model.LogEntries;
import com.meadowhawk.homepi.model.PiProfile;
import com.meadowhawk.homepi.service.business.DeviceManagementService;
import com.meadowhawk.homepi.service.business.HomePiUserService;
import com.meadowhawk.homepi.util.StringUtil;
import com.meadowhawk.homepi.util.model.PublicRESTDoc;
import com.meadowhawk.homepi.util.model.PublicRESTDocMethod;

@Path("/homepi")
@Component
@PublicRESTDoc(serviceName = "HomePiService", description = "Pi focused management services specifically for Pis.")
public class HomePiRestService {
	private static Logger log = Logger.getLogger( HomePiRestService.class );
	private static final String ACCESS_TOKEN = "access_token";
	
	@Context UriInfo uriInfo;
	
	@Autowired
	ClassPathResource updateFile;

	@Autowired
	HomePiUserService userService;
	
	@Value("${update.mainFile}")
	String mainUpdateFileName;
	
	@Value("${update.mainFileVersion}")
	Integer mainUpdateFileVersion;
	
	
	@Autowired
	DeviceManagementService deviceManagementService;

	@POST
	@Path("/user/{user_id}/pi/{piSerialId}/api")
	@Produces(MediaType.APPLICATION_JSON)
	@PublicRESTDocMethod(endPointName="Update Pi API Key", description="Updated the API key for the Pi. This can only be called by an auth user. Sadly for security reasons the user has to change the API stored on the PI manually. Returns 204 if sucessful.", sampleLinks={"/homepi/pi/01r735ds720/reg/api/de4d9e75-d6b3-43d7-9fef-3fb958356ded"})
	public Response updatePiApiKey(@PathParam("user_id") String userId, @PathParam("piSerialId") String piSerialId, @HeaderParam(ACCESS_TOKEN) String authToken) {
		deviceManagementService.updateApiKey(userId, authToken, piSerialId);
		return Response.noContent().build();
	}

	@GET
	@Path("/user/{user_id}")
	@Produces(MediaType.APPLICATION_JSON)
	@PublicRESTDocMethod(endPointName = "User Profile", description = "Retrieve user profile. Include access_token in head to gain owner view.", sampleLinks = { "/user/profile/test_user" })
	public Response getUser(@PathParam("user_id") String userId, @HeaderParam(ACCESS_TOKEN) String authToken){
		if(!StringUtil.isNullOrEmpty(userId)){
			//get authfrom request or set to null
			HomePiUser hUser = userService.getUserData(userId, authToken);
			return Response.ok(hUser).build(); 
		} else {
			throw new HomePiAppException(Status.NOT_FOUND,"Invalid user ID");
		}
		
	}
	
	@GET
	@Path("/user/{user_id}/pi/{piSerialId}/log")
	@Produces(MediaType.APPLICATION_JSON)
	@PublicRESTDocMethod(endPointName="Log Pi Message", description="Retrieves logs entries for given Pi. Pi API key or user auth may be required.", sampleLinks={"/homepi/pi/01r735ds720/log"})
	public List<LogEntries> getLlogs(@PathParam("piSerialId") String piSerialId){
		
		//Need to add params and privacy filtering on this.
		return new ArrayList<LogEntries>();
	}
	
	
	//ProfileManagement
	@GET
	@Path("/user/{user_id}/pi/{piSerialId}")
	@Produces(MediaType.APPLICATION_JSON)
	@PublicRESTDocMethod(endPointName = "Get PiProfile", description = "Retrieve pi profile by pi serial id. Include access_token for owner view.", sampleLinks = { "/homepi/user/test_user/pi/tu12345" })
	public Response getUserPiProfile(@PathParam("user_id") String userName,@PathParam("piSerialId") String piSerialId, @HeaderParam(ACCESS_TOKEN) String authToken){
		PiProfile profile = userService.getPiProfile(userName, authToken,piSerialId);
		return Response.ok(profile).build();
	}
	
	@POST
	@Path("/user/{user_id}/pi/{piSerialId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@PublicRESTDocMethod(endPointName = "Update PiProfile", description = "Retrieve users pi profiles. access_token is required or returns NoAccess.", sampleLinks = { "/homepi/user/test_user/pi/tu12345" })
	public Response updatePiProfile(@PathParam("user_id") String userName,@PathParam("piSerialId") String piSerialId, @HeaderParam(ACCESS_TOKEN) String authToken, PiProfile piProfile){
		userService.updatePiProfile(userName, authToken,piSerialId, piProfile);
		log.debug("Redirection to:"+getUriRedirect("getUserPiProfile"));
//		return Response.seeOther(getUriRedirect("getUserPiProfile")).build();
		return Response.noContent().build();
	}
	
	protected URI getUriRedirect(String methodName){
		UriBuilder ub = uriInfo.getBaseUriBuilder().path(HomePiRestService.class);
		URI redirectURI = ub.path(HomePiRestService.class, methodName).build();
		
		return redirectURI;
	}
	
	@GET
	@Path("/user/{user_id}/pi")
	@Produces(MediaType.APPLICATION_JSON)
	@PublicRESTDocMethod(endPointName = "Get User PiProfiles", description = "Retrieve users pi profiles. Include access_token in head to gain owner view.", sampleLinks = { "/homepi/user/test_user/pi" })
	public Response getUserPiProfiles(@PathParam("user_id") String userId, @HeaderParam(ACCESS_TOKEN) String authToken){
		HomePiUser hUser = userService.getUserData(userId, authToken);
		return Response.ok(hUser.getPiProfiles()).build();
	}
	
	
	
	//TODO: add string replace on py file to update the version number
	@GET
	@Path("/update")
	@Produces("text/x-python")
	@PublicRESTDocMethod(endPointName="Update Pi", description="EndPoint a Pi will call to request updates. Pi API key is required.", sampleLinks={"/homepi/pi/update"})
	@Deprecated
	public Response getScriptUpdate(){
		//TODO: Remove this once PY script is updated.
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