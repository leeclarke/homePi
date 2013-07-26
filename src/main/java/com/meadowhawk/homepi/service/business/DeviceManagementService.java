package com.meadowhawk.homepi.service.business;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.persistence.EntityExistsException;
import javax.persistence.NoResultException;
import javax.ws.rs.core.Response.Status;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.meadowhawk.homepi.dao.AbstractJpaDAO.DBSortOrder;
import com.meadowhawk.homepi.dao.HomePiUserDAO;
import com.meadowhawk.homepi.dao.LogDataDAO;
import com.meadowhawk.homepi.dao.PiProfileDAO;
import com.meadowhawk.homepi.exception.HomePiAppException;
import com.meadowhawk.homepi.model.HomePiUser;
import com.meadowhawk.homepi.model.LogData;
import com.meadowhawk.homepi.model.ManagedApp;
import com.meadowhawk.homepi.model.PiProfile;
import com.meadowhawk.homepi.util.StringUtil;
import com.meadowhawk.homepi.util.service.ApiKeyRequired;
import com.meadowhawk.homepi.util.service.ApiKeyRequiredBeforeException;
import com.meadowhawk.homepi.util.service.ApiKeyRequiredException;
import com.meadowhawk.homepi.util.service.AuthRequiredBeforeException;

/**
 * Service that is used for providing data specifically to the Raspberry Pi device. All Calls should enforce an API verification for security reasons. 
 * @author lee
 */
@Component
public class DeviceManagementService {

	@Autowired
	PiProfileDAO piProfileDao;
	
	@Autowired
	LogDataDAO logDataDAO;

	@Autowired
	HomePiUserDAO userDAO;
	
	/**
	 * @param serialId
	 * @return
	 * @throws HomePiAppException
	 */
	protected PiProfile getPiProfile(String serialId) throws HomePiAppException {
		try{
			return piProfileDao.findByPiSerialId(serialId);
		} catch(NoResultException nre){
			throw new HomePiAppException(Status.NOT_FOUND, "No matching serial ID was found.");
		} catch(Exception e){
			throw new HomePiAppException(Status.BAD_REQUEST, e);
		}
	}

	@ApiKeyRequired
	public PiProfile getPiProfile(String deviceId, String apiKey) {
		return getPiProfile(deviceId);
	}

	/**
	 * @return
	 * @throws HomePiAppException
	 */
//	@ApiKeyRequired
	//TODO: EVAL: NOt currently in use. I can't think of a reason for Pis to know about other Pis's...  yet.  Also API keys, only grant access to one profile.
	@Deprecated
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
	public PiProfile createPiProfile(String piSerialId, String ipAddress, String userName) throws HomePiAppException {
		if(StringUtil.isNullOrEmpty(piSerialId)){
			throw new HomePiAppException(Status.BAD_REQUEST, "Invalid Pi Serial ID.");
		}
		try{
			PiProfile piProfile = new PiProfile();
			piProfile.setPiSerialId(piSerialId);
			piProfile.setCreateTime(new DateTime());
			piProfile.setIpAddress(ipAddress);
			piProfile.setName("PI-"+piSerialId); //sets default name
			piProfile.setUserId(getUid(userName));
			
			piProfileDao.save(piProfile);
			
			//Add apiKey to the entry.
			piProfileDao.updateUUID(piProfile);
			
			return piProfileDao.findOne(piProfile.getPiId());
		} catch(EntityExistsException eee){
			throw new HomePiAppException(Status.BAD_REQUEST, piSerialId + ": This PI has already been registered");
		} catch(Exception e){
			throw new HomePiAppException(Status.BAD_REQUEST, e);
		}
	}

	/**
	 * Using the user name, look up the id so that it can be set properly. if null or invalid then just insert default.
	 * @param userName
	 * @return  - user id or 0 i invalid name.
	 */
	private Long getUid(String userName) {
		Long uid = 0L;
		try{
			if(!StringUtil.isNullOrEmpty(userName)){
				HomePiUser user = userDAO.findByUserName(userName);
				uid = user.getUserId();
			}
		} catch(Exception e){
			//any failure results in uid == 0
		}
		return uid;
	}

	/**
	 * Update Pi Profile
	 * @param piProfile
	 * @return
	 * @throws HomePiAppException
	 */
	@ApiKeyRequiredBeforeException
	public int updatePiProfile(PiProfile piProfile) throws HomePiAppException {
		try{
			piProfileDao.update(piProfile);
			return 1;
		} catch(Exception e){
			throw new HomePiAppException(Status.NOT_MODIFIED, e);
		}
	}

	/**
	 * Request new ApiKey using current api key credentials. Expected use by the Pi.
	 * @param apiKey - apiKey
	 * @param piSerialId - piSerialId
	 * @throws Exception if update fails
	 */
	@ApiKeyRequiredBeforeException
	public void updateApiKey(String piSerialId, String apiKey) {
		try{
			PiProfile profile = piProfileDao.findByPiSerialId(piSerialId);
			profile.setApiKey(UUID.fromString(apiKey));
			int stat = piProfileDao.updateUUID(profile);
			if(stat != 1){
				throw new HomePiAppException(Status.NOT_MODIFIED, "Update failed");
			}
		}
		catch (NoResultException nre) {
			throw new HomePiAppException(Status.NOT_FOUND, piSerialId + ": is not valid");
		} catch (HomePiAppException h) {
			throw h;
		}	catch (Exception e) {
			throw new HomePiAppException(Status.BAD_REQUEST, e);
		}
	}

	/**
	 * Request new ApiKey using User credentials. Expected use by the UI.
	 * @param userName - userName
	 * @param authToken - authToken
	 * @param piSerialId - piSerialId
	 * @throws Exception if failed.
	 */
	@AuthRequiredBeforeException
	public void updateApiKey(String userName, String authToken, String piSerialId) {
		try{
			PiProfile profile = piProfileDao.findByPiSerialId(piSerialId);
			int stat = piProfileDao.updateUUID(profile);
			if(stat != 1){
				throw new HomePiAppException(Status.NOT_MODIFIED, "Update failed");
			}
		}
		catch (NoResultException nre) {
			throw new HomePiAppException(Status.NOT_FOUND, piSerialId + ": is not valid");
		} catch (HomePiAppException h) {
			throw h;
		} catch (Exception e) {
			throw new HomePiAppException(Status.BAD_REQUEST, e);
		}
	}
	
	/**
	 * Verifies that a given serialId and ApiKey Match.
	 * @param piSerialId
	 * @param apiKey
	 * @return - true if valid
	 */
	public boolean validateApiKey(String piSerialId, String apiKey) {
		try{
			PiProfile profile = this.getPiProfile(piSerialId, apiKey);
			return (profile.getApiKey().equals(apiKey));
		} catch(Exception e){
			return false;
		}
	}

	/**
	 * Creates LogData from the Pi.
	 * @param piSerialId
	 * @param apiKey
	 * @param logData 
	 */
	@ApiKeyRequiredBeforeException
	public void createLogData(String piSerialId, String apiKey, LogData logData) {
		if(logData == null){
			throw new HomePiAppException(Status.BAD_REQUEST, "Invalid data, nothing to save.");
		}
		//Validate
		if(StringUtil.isNullOrEmpty(logData.getLogKey()) || StringUtil.isNullOrEmpty(logData.getLogMessage())){
			throw new HomePiAppException(Status.BAD_REQUEST,"logKey and logData are required fields and can not be null or empty values.");
		}
		PiProfile profile = getPiProfile(piSerialId);
		logData.setPiId(profile.getPiId());
		logData.setUserId(profile.getUserId());
		if(logData.getLogTypeId() == null){
			logData.setLogTypeId(1L); //Defaults to SYSTEM
		}
		if(logData.getCreateTime() == null){
			logData.setCreateTime(new DateTime());
		}
		
		try{
			logDataDAO.save(logData);
		} catch(Exception e){
			throw new HomePiAppException(Status.NOT_MODIFIED, "Can't create: " + e.getLocalizedMessage());
		}
	}

	@ApiKeyRequiredBeforeException
	public List<LogData> getLogData(String piSerialId, String apiKey, Map<WEB_PARAMS_LOG_DATA, Object> params) {
		PiProfile profile = getPiProfile(piSerialId);
		Long appId = null;
		if(params.containsKey(WEB_PARAMS_LOG_DATA.APP_NAME)){
			ManagedApp ma = profile.getManagedAppByName((String)params.get(WEB_PARAMS_LOG_DATA.APP_NAME));
			appId = ma.getAppId();
		}
		
		String logKey = (params.containsKey(WEB_PARAMS_LOG_DATA.LOG_KEY))? (String)params.get(WEB_PARAMS_LOG_DATA.LOG_KEY):null;
		String logType= (params.containsKey(WEB_PARAMS_LOG_DATA.LOG_TYPE))? (String)params.get(WEB_PARAMS_LOG_DATA.LOG_TYPE):null;
		return logDataDAO.findByDynamicParams(piSerialId, appId, logKey, logType, DBSortOrder.DESC);
	}
}
