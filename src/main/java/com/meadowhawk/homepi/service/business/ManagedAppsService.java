package com.meadowhawk.homepi.service.business;

import java.util.List;

import javax.ws.rs.core.Response.Status;

import org.apache.log4j.Logger;
import org.eclipse.jetty.util.log.Log;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.meadowhawk.homepi.dao.ManagedAppDAO;
import com.meadowhawk.homepi.exception.HomePiAppException;
import com.meadowhawk.homepi.model.HomePiUser;
import com.meadowhawk.homepi.model.ManagedApp;
import com.meadowhawk.homepi.util.StringUtil;
import com.meadowhawk.homepi.util.service.AuthRequiredBeforeException;
import com.meadowhawk.homepi.util.service.MaskData;

/**
 * Business logic specific to the ManagedApps for a given Pi and/or user.
 * @author lee
 */
@Component
public class ManagedAppsService {
	private static Logger log = Logger.getLogger(ManagedAppsService.class);
	
	@Autowired
	ManagedAppDAO managedAppDao;
	
	@Autowired
	HomePiUserService homePiUserService;
	
	/**
	 * 
	 * @param piSerialId
	 * @return
	 */
	//TODO Implement if needed
	public List<ManagedApp> getManagedAppsForDevice(String piSerialId) {
		return null;//managedAppDao.getManagedApps(piSerialId);
	}

	/**
	 * Finds App by the app name.
	 * @param userName
	 * @param authToken
	 * @param appName
	 * @return
	 */
	@MaskData
	public ManagedApp getUserApp(String userName, String authToken, String appName) {
		return getManagedApp(userName, authToken, appName);
	}

	/**
	 * Finds App by the web name, this is preferred as it escapes the spaces and is a bit nicer in the URI generation.
	 * @param userName
	 * @param authToken
	 * @param webName
	 * @return
	 */
	@MaskData
	public ManagedApp getUserAppByWebName(String userName, String authToken, String webName) {
		HomePiUser user = homePiUserService.getUserData(userName,authToken);
		try{
			return managedAppDao.findByWebName(webName, user.getUserId());
		} catch(Exception e){
			log.debug(e);
			throw new HomePiAppException(Status.NOT_FOUND);
		}
	}
	
	/**
	 * Updates existing app info.
	 * @param userName
	 * @param authToken
	 * @param appName
	 * @param managedApp
	 * @return
	 */
	@AuthRequiredBeforeException
	public int updateUserApps(String userName, String authToken, String appName,	ManagedApp managedApp) {
		ManagedApp ma = getManagedApp(userName, authToken, appName);
		if(!StringUtil.isNullOrEmpty(managedApp.getAppName())){
			ma.setAppName(managedApp.getAppName());
		}
		ma.setDeploymentPath(managedApp.getDeploymentPath());
		ma.setFileName(managedApp.getFileName());
		ma.setVersionNumber(managedApp.getVersionNumber());
		ma.setUpdateTime(new DateTime());
		
		try{
			managedAppDao.update(ma);
			return 1;
		} catch(Exception e){
			throw new HomePiAppException(Status.NOT_MODIFIED, e);
		}
	}
	
	/**
	 * Deletes a managed app if user auth is valid.
	 * @param userName
	 * @param authToken
	 * @param appName
	 */
	@AuthRequiredBeforeException
	public void deleteManageApp(String userName, String authToken, String appName) {
		ManagedApp managedApp = getManagedApp(userName, authToken, appName);
		managedAppDao.delete(managedApp);
	}
	
	/**
	 * Unprotected helper method, it is expected that security was enforced at the public method and would be redundant here.
	 * @param userName
	 * @param appName
	 * @return
	 */
	private ManagedApp getManagedApp(String userName, String authToken, String appName) throws HomePiAppException{
		HomePiUser user = homePiUserService.getUserData(userName,authToken);
		try{
			return managedAppDao.findByName(appName, user.getUserId());
		} catch(Exception e){
			log.debug(e);
			throw new HomePiAppException(Status.NOT_FOUND);
		}
	}

	/**
	 * @param userName
	 * @param authToken
	 * @param managedApp
	 */
	@AuthRequiredBeforeException
	public ManagedApp createUserApp(String userName, String authToken, ManagedApp managedApp) {
		if(managedApp == null || StringUtil.isNullOrEmpty(managedApp.getAppName())){
			throw new HomePiAppException(Status.BAD_REQUEST,"Invalid input, App Name missing.");
		}
		
		try{
			HomePiUser user = homePiUserService.getUserData(userName,null);
			managedApp.setOwnerId(user.getUserId());
			managedApp.setCreateTime(new DateTime());
			managedAppDao.save(managedApp);
		} catch(Exception e){
			log.debug(e);
			throw new HomePiAppException(Status.NOT_FOUND);
		}
		
		return getManagedApp(userName, authToken,  managedApp.getAppName());
	}

	/**
	 * Ensures Masking of the ManagedApp data 
	 * @param userName
	 * @param authToken
	 * @return
	 */
	@MaskData
	public List<ManagedApp> getUserApps(String userName, String authToken) {
		HomePiUser user = homePiUserService.getUserData(userName,authToken);
		return user.getManagedApps();
	}
}
