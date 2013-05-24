package com.meadowhawk.homepi.dao;

import java.util.List;

import org.springframework.stereotype.Component;

import com.meadowhawk.homepi.model.LogData;

@Component
public class LogDataDAO extends AbstractJpaDAO< LogData > {

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
	public List<LogData> findByLogType(String logTypeName) {
		return entityManager.createNamedQuery("LogData.findByLogType",this.clazz).setParameter("logTypeName", logTypeName).getResultList();
	}
}
