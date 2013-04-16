package com.meadowhawk.homepi.service.business;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.meadowhawk.homepi.dao.ManagedAppDAO;
import com.meadowhawk.homepi.model.ManagedApp;

/**
 * Business logic specific to the ManagedApps for a given Pi and/or user.
 * @author lee
 */
@Component
public class ManagedAppsService {

	@Autowired
	ManagedAppDAO managedAppDao;
	
	public List<ManagedApp> getManagedAppsForDevice(String piSerialId) {
		//TODO: verify user access
		return managedAppDao.getManagedApps(piSerialId);
	}
	
}
