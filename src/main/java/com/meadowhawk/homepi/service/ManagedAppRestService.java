package com.meadowhawk.homepi.service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.meadowhawk.homepi.service.business.DeviceManagementService;
import com.meadowhawk.homepi.service.business.ManagedAppsService;
import com.meadowhawk.homepi.util.model.PublicRESTDoc;
import com.meadowhawk.homepi.util.model.PublicRESTDocMethod;
import com.meadowhawk.homepi.util.model.TODO;

/**
 * All interaction with ManagedApps will be done through this endPoint.
 * @author lee
 */
@Path("/homepi/pi/apps")
@Component
@PublicRESTDoc(serviceName = "ManagedAppRestService", description = "Pi application endPoints.")
public class ManagedAppRestService {

	//TODO: Determine if this is even needed!!
	@Autowired
	DeviceManagementService deviceManagementService;
	
	@Autowired
	@Qualifier("managedAppsService")
	ManagedAppsService managedAppsService;
	
	@GET
	@Path("/{piSerialId}")
	@Produces(MediaType.APPLICATION_JSON)
	@PublicRESTDocMethod(endPointName="List Apps", description="", sampleLinks={"./homepi/pi/apps/01r735ds720"})
	public Response getApps(@PathParam("piSerialId") String piSerialId){ //TODO: Why is this a String?
		//TODO: Should included user auth/id in this request once defined.
//		return managedAppsService.getManagedAppsForDevice(piSerialId);		
		return Response.ok(new TODO()).build();
	}
	
	/* TODO:
	 * get ManagedApp but ID & piId or by User ID?
	 * add/update/delete App
	 * uploadArchivefile
	 * getArchiveInfo
	 * getArchive
	 */
}
