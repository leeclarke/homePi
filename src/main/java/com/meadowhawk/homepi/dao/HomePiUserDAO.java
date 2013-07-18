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

	/**
	 * @param userId
	 * @return
	 * @throws NoResultException
	 */
	public HomePiUser findByUserId(Long userId) {
		return entityManager.createNamedQuery("HomePiUser.findByUserId",this.clazz).setParameter("uid", userId).getSingleResult();
	}

	/**
	 * Verifies user by checking user name and auth token.
	 * @param userName
	 * @param authToken
	 * @return true if valid
	 */
	public boolean authorizeToken(String userName, String authToken) {
		Long ct = entityManager.createNamedQuery("HomePiUser.authToken",Long.class).setParameter("userName", userName).setParameter("authToken", authToken).getSingleResult();
		return (ct==1)?true:false;
	}


	/**
	 * Verifies user by checking userId and auth token.
	 * @param userId
	 * @param authToken
	 * @return
	 */
	public boolean authorizeToken(Long userId, String authToken) {
		try{
			HomePiUser user = this.findByUserId(userId);
			return this.authorizeToken(user.getUserName(), authToken);
		}
		catch(Exception e ){
			return false;
		}
	}

}

	