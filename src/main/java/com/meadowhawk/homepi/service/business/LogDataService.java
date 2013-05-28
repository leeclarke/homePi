package com.meadowhawk.homepi.service.business;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response.Status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.meadowhawk.homepi.dao.LogDataDAO;
import com.meadowhawk.homepi.dao.PiProfileDAO;
import com.meadowhawk.homepi.exception.HomePiAppException;
import com.meadowhawk.homepi.model.LogData;
import com.meadowhawk.homepi.util.StringUtil;
import com.meadowhawk.homepi.util.service.ApiKeyRequiredBeforeException;

@Component
public class LogDataService {

	@Autowired
	LogDataDAO logDataDAO;
	
	@Autowired
	PiProfileDAO piProfileDAO;
	
	public enum SEARCH_TYPE {
		LOG_KEY, LOG_TYPE, PI_SERIAL
	}

//TODO Allow Logs to be specified Private by user. Perhaps enforce by LogType?  SYSTEM type errors might be best kept private.
//	@MaskData  	
	/**
	 * Provides search based on search type. All responses are treated the same way with the same security, just need to specify search type.
	 * @param type - type of search to perform.
	 * @param param - search param.
	 * @return list of LogData
	 */
	public List<LogData> getLogDataByKey(SEARCH_TYPE type, String param){
		List<LogData> result = new ArrayList<LogData>();
		switch (type) {
			case LOG_KEY:
				result = logDataDAO.findByLogKey(param);
				break;
			case LOG_TYPE:
				result = logDataDAO.findByLogType(param);
				break;
			case PI_SERIAL:
				result = logDataDAO.findBySerialId(param);
				break;
		}
		
		return result;
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
