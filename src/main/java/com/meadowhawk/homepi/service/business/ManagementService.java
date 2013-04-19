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
//		TODO: Add Exception handling!!
		return piProfileDao.findByPiSerialId(serialId);
	}

	/**
	 * @return
	 * @throws HomePiAppException
	 */
	public List<PiProfile> getAllPiProfiles() throws HomePiAppException {
		//TODO: Add Exception handling!!
		return piProfileDao.findAll();
	}	
	
	/**
	 * The create inserts a new row if none is present and then returns the result populated with what data the request provided. 
	 * @param piSerialId
	 * @param ipAddress
	 * @return
	 * @throws HomePiAppException
	 */
	public PiProfile createPiProfile(String piSerialId, String ipAddress) throws HomePiAppException {
//		TODO: Add Exception handling!!
		PiProfile piProfile = new PiProfile();
		piProfile.setPiSerialId(piSerialId);
		piProfile.setCreateTime(new DateTime());
		piProfile.setIpAddress(ipAddress);
		piProfileDao.save(piProfile);
		
		return piProfile;
	}

	public int updatePiProfile(PiProfile piProfile) throws HomePiAppException {
		//TODO:  Deug this it isnt working.
//		TODO: Add Exception handling!!
		piProfileDao.update(piProfile);
		return 1;  //TODO: Remove
	}
	
}
