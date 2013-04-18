package com.meadowhawk.homepi.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Response.Status;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.stereotype.Component;

import com.meadowhawk.homepi.exception.HomePiAppException;
import com.meadowhawk.homepi.model.ManagedApp;

/**
 * @author lee
 */
@Component
public class ManagedAppDAO extends NamedParameterJdbcDaoSupport{

	private static final String SELECT_MANAGED_APPS = "SELECT * FROM MANAGED_APP m, USER_PI_MANAGED_APP u, PI_PROFILE p " +
			"WHERE p.PI_SERIAL_ID = :piSerialId  AND p.PI_ID = u.PI_ID AND u.APP_ID = m.APP_ID" ;
	
	public List<ManagedApp> getManagedApps(String piSerialId) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("piSerialId", piSerialId);
		
		try{
			return getNamedParameterJdbcTemplate().query(SELECT_MANAGED_APPS, paramMap, new HomePiBeanPropertyRowMapper<ManagedApp>(ManagedApp.class));
		} catch (EmptyResultDataAccessException e) {
			throw new HomePiAppException(Status.NOT_FOUND, "Managed Apps assigned to that PI id was not found.");
		} catch (Exception e) {
			throw new HomePiAppException(Status.BAD_REQUEST, e.getMessage());
		}
	}
	
	
	
}
