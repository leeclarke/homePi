package com.meadowhawk.homepi.service.business;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Response.Status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.meadowhawk.homepi.dao.LogDataDAO;
import com.meadowhawk.homepi.dao.PiProfileDAO;
import com.meadowhawk.homepi.exception.HomePiAppException;
import com.meadowhawk.homepi.model.LogData;
import com.meadowhawk.homepi.model.ManagedApp;
import com.meadowhawk.homepi.util.StringUtil;
import com.meadowhawk.homepi.util.service.ApiKeyRequiredBeforeException;
import com.meadowhawk.homepi.util.service.MaskData;

@Component
public class LogDataService {

	public enum WEB_PARAMS{
		APP_NAME("app_name"),LOG_KEY("log_key"),LOG_TYPE("log_Type");
		
		private String queryPram;
		
		WEB_PARAMS(String value){
			this.queryPram = value;
		}
		
		public String webParam(){
			return this.queryPram;
		}
		
	}
	
	@Autowired
	LogDataDAO logDataDAO;
	
	@Autowired
	PiProfileDAO piProfileDAO;
	
	@Autowired
	ManagedAppsService managedAppsService;
	
	public enum SEARCH_TYPE {
		LOG_TYPE, PI_SERIAL, DYNAMIC
	}

//TODO Allow Logs to be specified Private by user. Perhaps enforce by LogType?  SYSTEM type errors might be best kept private.
	  	
	/**
	 * All Log searches for this call require a piSerialId additional params can be specified in the map. Searches can be refined by 
	 * app_name as well as log_Type and log_key.
	 * 
	 * Provides search based on search type. All responses are treated the same way with the same security, just need to specify search type.
	 * @param type - type of search to perform.
	 * @param param - search param.
	 * @return list of LogData
	 */
	@MaskData
	public List<LogData> getLogDataBySearchType(String userName, String authToken, String piSerialId, SEARCH_TYPE type, Map<WEB_PARAMS, Object> params) {
		List<LogData> result = new ArrayList<LogData>();
		switch (type) {
			case LOG_TYPE:
				result = logDataDAO.findByLogType(piSerialId, (String) params.get(WEB_PARAMS.LOG_TYPE));
				break;
			case PI_SERIAL:
				result = logDataDAO.findBySerialId(piSerialId);
				break;
			case DYNAMIC:
				Long appId = getAppId(userName, authToken, (String) params.get(WEB_PARAMS.APP_NAME));
				String logKey = (params.containsKey(WEB_PARAMS.LOG_KEY) && params.get(WEB_PARAMS.LOG_KEY) != null) ? (String) params.get(WEB_PARAMS.LOG_KEY) : null;
				String logType = (params.containsKey(WEB_PARAMS.LOG_TYPE) && params.get(WEB_PARAMS.LOG_TYPE) != null) ? (String) params.get(WEB_PARAMS.LOG_TYPE) : null;
				result = logDataDAO.findByDynamicParams(piSerialId, appId, logKey, logType, null);
				break;
		}

		return result;
	}
	
	private Long getAppId(String userName, String authToken, String appName) {
		ManagedApp app = null;
		if(!StringUtil.isNullOrEmpty(appName)){
			 app =managedAppsService.getUserAppByWebName(userName, authToken , appName);
		}
		return (app != null)?app.getAppId():null;
	}	

	/**
	 * Create new log data if the PI provides a valid apiKey.
	 * @param serialId - serialId
	 * @param apiKey - apiKey
	 * @param logData - logData
	 */
	@ApiKeyRequiredBeforeException
	public void createLogData(String serialId, String apiKey, LogData logData) {
		//Validate
		List<String> errors = validateLogData(logData);
		if(!errors.isEmpty()){
			HomePiAppException ex = new HomePiAppException(Status.BAD_REQUEST,"Invlaid data input.");
			ex.setModel(errors);
			throw ex; 
		}
		
		//Need to look up userId
		logData.setUserId(piProfileDAO.findByPiSerialId(serialId).getUserId());
		
		logDataDAO.save(logData);
	}
	
	private List<String> validateLogData(LogData logData){
		List<String> errors = new ArrayList<String>();
		if(logData == null){
			errors.add("Missing log data.");  //TODO: Extract Error messages.
		} else{
			if(StringUtil.isNullOrEmpty(logData.getLogKey())){
				errors.add("Missing log key.");
			}
			if(StringUtil.isNullOrEmpty(logData.getLogMessage())){
				errors.add("Missing log message/value.");
			}
			if(logData.getAppId() == null || logData.getAppId()<1){
				errors.add("Invalid or missing AppId, value required.");
			}
		}
		return errors;
	}
}
