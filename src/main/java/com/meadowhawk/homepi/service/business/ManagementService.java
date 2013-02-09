package com.meadowhawk.homepi.service.business;

import java.util.List;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.meadowhawk.homepi.dao.PiProfileDAO;
import com.meadowhawk.homepi.exception.HomePiAppException;
import com.meadowhawk.homepi.model.PiProfile;

@Component
public class ManagementService {

	@Autowired
	PiProfileDAO piProfileDao;
	
	/**
	 * @param serialId
	 * @return
	 * @throws HomePiAppException
	 */
	public PiProfile getPiProfile(String serialId) throws HomePiAppException {
		return piProfileDao.getPiProfile(serialId);
	}

	/**
	 * @return
	 * @throws HomePiAppException
	 */
	public List<PiProfile> getAllPiProfiles() throws HomePiAppException {
		return piProfileDao.getAllPiProfiles();
	}	
	
	/**
	 * The create inserts a new row if none is present and then returns the result populated with what data the request provided. 
	 * @param piSerialId
	 * @param ipAddress
	 * @return
	 * @throws HomePiAppException
	 */
	public PiProfile createPiProfile(String piSerialId, String ipAddress) throws HomePiAppException {
		PiProfile piProfile = new PiProfile();
		piProfile.setPiSerialId(piSerialId);
		piProfile.setCreateTime(new DateTime());
		piProfile.setIpAddress(ipAddress);
		int resp = piProfileDao.createPiProfile(piProfile);
		if(resp > 0) {
			return piProfileDao.getPiProfile(piSerialId);
		} else {
			throw new HomePiAppException("Failed to create New Profile for Pi:" + piSerialId);
		}
	}

	public int updatePiProfile(PiProfile piProfile) throws HomePiAppException {
		//TODO:  Deug this it isnt working.
		return piProfileDao.updatePiProfile(piProfile);
		
	}
	
}
