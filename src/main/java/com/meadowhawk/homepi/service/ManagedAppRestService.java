package com.meadowhawk.homepi.service;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.meadowhawk.homepi.model.ManagedApp;
import com.meadowhawk.homepi.service.business.ManagedAppsService;
import com.meadowhawk.homepi.service.business.ManagementService;

/**
 * All interaction with ManagedApps will be done through this endPoint.
 * @author lee
 */
@Path("/homepi/pi/apps")
@Component
public class ManagedAppRestService {

	@Autowired
	@Qualifier("managementService")
	ManagementService managementService;
	
	@Autowired
	@Qualifier("managedAppsService")
	ManagedAppsService managedAppsService;
	
	@GET
	@Path("/{piSerialId}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<ManagedApp> getApps(@PathParam("piSerialId") String piSerialId){ //TODO: Why is this a String?
		//TODO: Should included user auth/id in this request once defined.
		return managedAppsService.getManagedAppsForDevice(piSerialId);		
	}
	
	/* TODO:
	 * get ManagedApp but ID & piId or by User ID?
	 * add/update/delete App
	 * uploadArchivefile
	 * getArchiveInfo
	 * getArchive
	 */
}
