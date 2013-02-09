package com.meadowhawk.homepi.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Response.Status;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;

import com.meadowhawk.homepi.exception.HomePiAppException;
import com.meadowhawk.homepi.model.PiProfile;

/**
 * @author lee
 */
@Component
public class PiProfileDAO extends NamedParameterJdbcDaoSupport{
	
	
	private static final String INSERT_PROFILE = "INSERT INTO pi_profile (pi_serial_id, name, ip_address, ssh_port_number, update_time) " +
			"VALUES (:piSerialId, :name, :ipAddress, :sshPortNumber, now())";
	
	private static final String UPDATE_PROFILE = "UPDATE pi_profile SET update_time = now(), name = :name, ip_address = :ipAddress, " +
			"ssh_port_number = :sshPortNumber WHERE pi_serial_id = :piSerialId";
			
	private static final String SELECT_PI_PROFILE = "SELECT * from PI_PROFILE where PI_SERIAL_ID = :piSerialId";

	private static final String DELETE_PI_PROFILE = "DELETE FROM pi_profile WHERE pi_serial_id = :piSerialId";

	private static final String SELECT_ALL_PI_PROFILE = "SELECT * from PI_PROFILE order by update_time desc";


	/**
	 * @param serialId
	 * @return
	 * @throws HomePiAppException 
	 */
	public PiProfile getPiProfile(String serialId) throws HomePiAppException {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("piSerialId", serialId);
		try{
			return getNamedParameterJdbcTemplate().queryForObject(SELECT_PI_PROFILE, paramMap, new HomePiBeanPropertyRowMapper<PiProfile>(PiProfile.class));
		} catch (EmptyResultDataAccessException e) {
			throw new HomePiAppException(Status.NOT_FOUND, "Profile with that PI id was not found.");
		} catch (Exception e) {
			throw new HomePiAppException(Status.BAD_REQUEST, e.getMessage());
		}
	}
	
	
	/**
	 * @param piProfile
	 * @return
	 */
	public int createPiProfile(PiProfile piProfile) throws HomePiAppException  {
		try{
			BeanPropertySqlParameterSource paramMap = new BeanPropertySqlParameterSource(piProfile);
			
			return getNamedParameterJdbcTemplate().update(INSERT_PROFILE, paramMap);
		} catch(DuplicateKeyException dke) {
			throw new HomePiAppException(Status.CONFLICT, "Failed to create Record","Device serial already registered.",0,null);
		} catch (Exception e) {
			throw new HomePiAppException("Failed to create Record.", e); 
		}
	}
	
	/**
	 * @param piProfile
	 * @return
	 */
	public int updatePiProfile(PiProfile piProfile) throws HomePiAppException {
		try{
			BeanPropertySqlParameterSource paramMap = new BeanPropertySqlParameterSource(piProfile);
			
			return getNamedParameterJdbcTemplate().update(UPDATE_PROFILE, paramMap);
		} catch (Exception e) {
			System.out.println("Failed update"+e);
//			logger.error("Failed update",e);
			throw new HomePiAppException("Failed to Update Record.", e); 
		}
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


	public List<PiProfile> getAllPiProfiles() {
		try{
			Map<String, Object> paramMap = new HashMap<String, Object>();
			return getNamedParameterJdbcTemplate().query(SELECT_ALL_PI_PROFILE, paramMap , new HomePiBeanPropertyRowMapper<PiProfile>(PiProfile.class));
		} catch (EmptyResultDataAccessException e) {
			throw new HomePiAppException(Status.NOT_FOUND, "Profile with that PI id was not found.");
		} catch (Exception e) {
			throw new HomePiAppException(Status.BAD_REQUEST, e.getMessage());
		}
	}
}
