package com.meadowhawk.homepi.dao;

import org.springframework.stereotype.Component;

import com.meadowhawk.homepi.model.HomePiUser;

@Component
public class HomePiUserDAO  extends AbstractJpaDAO< HomePiUser >{
	
	public HomePiUserDAO() {
		setClazz(HomePiUser.class );
	}


	/**
	 * @param userName
	 * @return
	 */
	public HomePiUser findByUserName(String userName){
		return entityManager.createNamedQuery("HomePiUser.findByUserName",this.clazz).setParameter("name", userName).getSingleResult();
	}
	
}
