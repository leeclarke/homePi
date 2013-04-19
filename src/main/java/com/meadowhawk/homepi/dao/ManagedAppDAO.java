package com.meadowhawk.homepi.dao;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.meadowhawk.homepi.model.ManagedApp;

/**
 * DAO for retrieving ManagedApps. Managed Apps can be defined and then assigned to more then one PiProfile.
 * @author lee
 */
@Component
public class ManagedAppDAO extends AbstractJpaDAO< ManagedApp >{
	
	public ManagedAppDAO(){
		setClazz(ManagedApp.class);
	}

	/**
	 * @param piSerialId
	 * @return
	 */
	public List<ManagedApp> getManagedApps(String piSerialId) {
		return entityManager.createNamedQuery("ManagedApp.findByPiSerialId",this.clazz).setParameter("piSerialId", piSerialId).getResultList();
	}
	
//	private static final String SELECT_MANAGED_APPS = "SELECT * FROM MANAGED_APP m, USER_PI_MANAGED_APP u, PI_PROFILE p " +
//			"WHERE p.PI_SERIAL_ID = :piSerialId  AND p.PI_ID = u.PI_ID AND u.APP_ID = m.APP_ID" ;
//	
//	public List<ManagedApp> getManagedApps(String piSerialId) {
//		Map<String, Object> paramMap = new HashMap<String, Object>();
//		paramMap.put("piSerialId", piSerialId);
//		
//		try{
//			return getNamedParameterJdbcTemplate().query(SELECT_MANAGED_APPS, paramMap, new HomePiBeanPropertyRowMapper<ManagedApp>(ManagedApp.class));
//		} catch (EmptyResultDataAccessException e) {
//			throw new HomePiAppException(Status.NOT_FOUND, "Managed Apps assigned to that PI id was not found.");
//		} catch (Exception e) {
//			throw new HomePiAppException(Status.BAD_REQUEST, e.getMessage());
//		}
//	}
	
	/**
	 * Override to ensure that mapping table gets updated to create piSerialId association.
	 * @see com.meadowhawk.homepi.dao.AbstractJpaDAO#save(java.lang.Object)
	 */
	@Override
	@Transactional
	public void save(ManagedApp entity) {
		super.save(entity);
		
		//assuming the above passed, create the association
//		ManagedAppMapping map = new ManagedAppMapping(entity);
//		entityManager.persist(map);		
	}
	
}
