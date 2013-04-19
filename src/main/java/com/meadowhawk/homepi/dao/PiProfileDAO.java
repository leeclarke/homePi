package com.meadowhawk.homepi.dao;

import org.joda.time.DateTime;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.meadowhawk.homepi.model.PiProfile;

/**
 * @author lee
 */
@Component
public class PiProfileDAO extends AbstractJpaDAO< PiProfile >{
	
	public PiProfileDAO(){
		setClazz(PiProfile.class);
	}
	
	public PiProfile findByPiSerialId(String piSerialId){
		return entityManager.createNamedQuery("PiProfile.findByPiSerialId",this.clazz).setParameter("piSerialId", piSerialId).getSingleResult();
	}
	
	@Override
	@Transactional
	public void update(PiProfile entity) {
		entity.setUpdateTime(new DateTime());
		super.update(entity);
	}
	
}
