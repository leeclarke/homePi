package com.meadowhawk.homepi.dao;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.DELETE;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.stereotype.Component;

import com.meadowhawk.homepi.model.PiProfile;

/**
 * @author lee
 */
@Component
public class PiProfileDAO extends NamedParameterJdbcDaoSupport{
	
	private static final String INSERT_PROFILE = "INSERT INTO pi_profile (pi_serial_id, name, ip_address, ssh_port_number) " +
			"VALUES (:piSerialId, :name, :ipAddress, :sshPortNumber)";
	
	private static final String UPDATE_PROFILE = "UPDATE pi_profile SET update_time = now(), name = :name, ip_address = :ipAddress, " +
			"ssh_port_number = :sshPortNumber WHERE pi_serial_id = :piSerialId";
			
	private static final String SELECT_PI_PROFILE = "SELECT * from PI_PROFILE where PI_SERIAL_ID = :piSerialId";

	private static final String DELETE_PI_PROFILE = "DELETE FROM pi_profile WHERE pi_serial_id = :piSerialId";


	/**
	 * @param serialId
	 * @return
	 */
	public PiProfile getPiProfile(String serialId) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("piSerialId", serialId);
		return getNamedParameterJdbcTemplate().queryForObject(SELECT_PI_PROFILE, paramMap, new HomePiBeanPropertyRowMapper<PiProfile>(PiProfile.class));
	}
	
	
	/**
	 * @param piProfile
	 * @return
	 */
	public int createPiProfile(PiProfile piProfile){
		BeanPropertySqlParameterSource paramMap = new BeanPropertySqlParameterSource(piProfile);
		
		return getNamedParameterJdbcTemplate().update(INSERT_PROFILE, paramMap);
	}
	
	/**
	 * @param piProfile
	 * @return
	 */
	public int updatePiProfile(PiProfile piProfile){
		BeanPropertySqlParameterSource paramMap = new BeanPropertySqlParameterSource(piProfile);
		
		return getNamedParameterJdbcTemplate().update(UPDATE_PROFILE, paramMap);
	}
	
	/**
	 * @param piProfile
	 * @return
	 */
	public int deletePiProfile(PiProfile piProfile) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("piSerialId", piProfile.getPiSerialId());
		return getNamedParameterJdbcTemplate().update(DELETE_PI_PROFILE, paramMap);
	}
}
