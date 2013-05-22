package com.meadowhawk.homepi.dao;

import java.util.List;

import javax.persistence.TypedQuery;

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
		return null; //entityManager.createNamedQuery("ManagedApp.findByPiSerialId",this.clazz).setParameter("piSerialId", piSerialId).getResultList();
	}
	
	/**
	 * @param appName
	 * @return
	 */
	public ManagedApp findByName(String appName, Long userId) {
		return entityManager.createNamedQuery("ManagedApp.findByAppName", this.clazz).setParameter("appName", appName).setParameter("userId", userId).getSingleResult();
	}
	
	/**
	 * @param webName
	 * @return
	 */
	public ManagedApp findByWebName(String webName, Long userId) {
		return entityManager.createNamedQuery("ManagedApp.findByAppWebName", this.clazz).setParameter("webName", webName).setParameter("userId", userId).getSingleResult();
	}
	
	/**
	 * Override to ensure that mapping table gets updated to create piSerialId association.
	 * @see com.meadowhawk.homepi.dao.AbstractJpaDAO#save(java.lang.Object)
	 */
	@Override
	@Transactional
	public void save(ManagedApp entity) {
		super.save(entity);
		
	}
	
}
