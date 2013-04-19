package com.meadowhawk.homepi.service.business;

import java.util.List;

import javax.persistence.EntityExistsException;
import javax.persistence.NoResultException;
import javax.ws.rs.core.Response.Status;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.meadowhawk.homepi.dao.PiProfileDAO;
import com.meadowhawk.homepi.exception.HomePiAppException;
import com.meadowhawk.homepi.model.PiProfile;
import com.meadowhawk.homepi.util.StringUtil;

/**
 * @author lee
 */
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
		try{
			return piProfileDao.findByPiSerialId(serialId);
		} catch(NoResultException nre){
			throw new HomePiAppException(Status.NOT_FOUND, "No matching serial ID was found.");
		} catch(Exception e){
			throw new HomePiAppException(Status.BAD_REQUEST, e);
		}
	}

	/**
	 * @return
	 * @throws HomePiAppException
	 */
	public List<PiProfile> getAllPiProfiles() throws HomePiAppException {
		try{
			return piProfileDao.findAll();
		} catch(NoResultException nre){
			throw new HomePiAppException(Status.NOT_FOUND, "No matching serial ID was found.");
		} catch(Exception e){
			throw new HomePiAppException(Status.BAD_REQUEST, e);
		}
	}	
	
	/**
	 * The create inserts a new row if none is present and then returns the result populated with what data the request provided. 
	 * @param piSerialId
	 * @param ipAddress
	 * @return
	 * @throws HomePiAppException
	 */
	public PiProfile createPiProfile(String piSerialId, String ipAddress) throws HomePiAppException {
		if(StringUtil.isNullOrEmpty(piSerialId)){
			throw new HomePiAppException(Status.BAD_REQUEST, "Invalid Pi Serial ID.");
		}
		try{
			PiProfile piProfile = new PiProfile();
			piProfile.setPiSerialId(piSerialId);
			piProfile.setCreateTime(new DateTime());
			piProfile.setIpAddress(ipAddress);
			piProfileDao.save(piProfile);
			
			return piProfile;
		} catch(EntityExistsException eee){
			throw new HomePiAppException(Status.BAD_REQUEST, piSerialId + ": This PI has already been registered");
		} catch(Exception e){
			throw new HomePiAppException(Status.BAD_REQUEST, e);
		}
	}

	/**
	 * @param piProfile
	 * @return
	 * @throws HomePiAppException
	 */
	public int updatePiProfile(PiProfile piProfile) throws HomePiAppException {
		try{
			piProfileDao.update(piProfile);
			return 1;
		} catch(Exception e){
			throw new HomePiAppException(Status.NOT_MODIFIED, e);
		}
	}
	
}
