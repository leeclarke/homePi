package com.meadowhawk.homepi.dao;

import java.util.List;
import java.util.Map;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;

import org.springframework.stereotype.Component;

import com.meadowhawk.homepi.model.LogData;
import com.meadowhawk.homepi.util.StringUtil;

@Component
public class LogDataDAO extends AbstractJpaDAO< LogData > {

	private static final String ORDER_BY_CREATE_TIME = " order by create_time ";
	private static final String LOG_KEY_WHERE = " and l.log_key LIKE :logKey ";
	private static final String DYNAMIC_LOG_SELECT = "SELECT l.log_id, l.pi_id, l.user_id, l.app_id, l.log_type_id, l.create_time, l.log_key, l.log_value FROM log_data l, pi_profile p, log_type t WHERE t.log_type_id = l.log_type_id AND l.pi_id = p.pi_id AND p.pi_serial_id = :piSerialId AND l.app_id = :appId";
	private static final String DYNAMIC_LOG_NO_APP_SELECT = "SELECT l.log_id, l.pi_id, l.user_id, l.app_id, l.log_type_id, l.create_time, l.log_key, l.log_value FROM log_data l, pi_profile p, log_type t WHERE t.log_type_id = l.log_type_id AND l.pi_id = p.pi_id AND p.pi_serial_id = :piSerialId";

	public LogDataDAO(){
		setClazz(LogData.class);
	}
	
	/**
	 * @param piSerialId
	 * @return
	 */
	public List<LogData> findBySerialId(String piSerialId) {
		return entityManager.createNamedQuery("LogData.findByPiSerialId",this.clazz).setParameter("piSerialId", piSerialId).getResultList();
	}
	
	/**
	 * @param logKey
	 * @return
	 */
	public List<LogData> findByLogKey(String logKey) {
		return entityManager.createNamedQuery("LogData.findByLogKey",this.clazz).setParameter("logKey", logKey+"%").getResultList();
	}

	/**
	 * @param logTypeName
	 * @return
	 */
	public List<LogData> findByLogType(String piSerialId, String logTypeName) {
		return entityManager.createNamedQuery("LogData.findByLogType",this.clazz).setParameter("piSerialId", piSerialId).setParameter("logTypeName", logTypeName).getResultList();
	}
	
	/**
	 * Dynamic query for LogData.
	 * @param piSerialId
	 * @param appId
	 * @param logType TODO
	 * @param sortOrder
	 * @param logTypeName
	 * @return
	 */
	//TODO: revisit this later
	//TODO: Add date range search params.
	@SuppressWarnings("unchecked")
	public List<LogData> findByDynamicParams(String piSerialId, Long appId, String logKey, String logType, DBSortOrder sortOrder) {
		StringBuilder q = new StringBuilder((appId == null)?DYNAMIC_LOG_NO_APP_SELECT:DYNAMIC_LOG_SELECT);

		if(!StringUtil.isNullOrEmpty(logType)){ //LogType Check
			q.append(" AND t.log_type_name = :logType");
		}

		if(!StringUtil.isNullOrEmpty(logKey)){
			q.append(LOG_KEY_WHERE);
		}
		
		q.append(ORDER_BY_CREATE_TIME).append((sortOrder == null)? DBSortOrder.DESC : sortOrder.toString());
		
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		builder.createQuery(this.clazz);
		
		Query query = entityManager.createNativeQuery(q.toString(), this.clazz).setParameter("piSerialId", piSerialId);
		
		if(appId != null){
			query.setParameter("appId", appId);
		}
		if(!StringUtil.isNullOrEmpty(logKey)){
			query.setParameter("logKey", logKey);
		}
		if(!StringUtil.isNullOrEmpty(logType)){
			query.setParameter("logType", logType);
		}
		
		return query.getResultList();
	}
	
	/**
	 * Simplified Dynamic.
	 * @param piSerialId
	 * @param appId
	 * @return	
	 */
	public List<LogData> findByDynamicParams(String piSerialId, long appId) {
		entityManager.getCriteriaBuilder();
		return entityManager.createNamedQuery("LogData.findBySerialAppId",this.clazz).setParameter("piSerialId", piSerialId).setParameter("appId", appId).getResultList();
	}

	/**
	 * Selects data for PI with given log Key.
	 * @param piSerialId
	 * @param appId
	 * @param logKey
	 * @return
	 */
	public List<LogData> findByDynamicParams(String piSerialId, long appId, String logKey) {
		return findByDynamicParams(piSerialId, appId,logKey ,null, null);
	}
}
