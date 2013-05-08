package com.meadowhawk.homepi.dao;

import javax.persistence.NoResultException;

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
	public HomePiUser findByUserName(String userName) throws NoResultException{
		return entityManager.createNamedQuery("HomePiUser.findByUserName",this.clazz).setParameter("name", userName).getSingleResult();
	}
	
	/**
	 * @param email
	 * @return
	 * @throws NoResultException
	 */
	public HomePiUser findByEmail(String email) throws NoResultException{
		return entityManager.createNamedQuery("HomePiUser.findByEmail",this.clazz).setParameter("email", email).getSingleResult();
	}


	public boolean authorizeToken(String userName, String authToken) {
		// TODO Auto-generated method stub
		return false;
	}
	
}
