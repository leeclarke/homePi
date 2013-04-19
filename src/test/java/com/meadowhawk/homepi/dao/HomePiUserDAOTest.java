package com.meadowhawk.homepi.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import javax.persistence.NoResultException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.meadowhawk.homepi.model.HomePiUser;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:localbeans.xml"})
public class HomePiUserDAOTest {

	@Autowired
	HomePiUserDAO homePiUserDAO;
	
	@Test
	public void testFindOne() {
		
		Long id = 1L;
		Object resp = homePiUserDAO.findOne(id );
	}

	@Test
	public void testFindAll() {
		List<HomePiUser> resp = homePiUserDAO.findAll();
		assertNotNull(resp);
		assertTrue(resp.size()>0);
	}

	@Test
	public void testFindByUserName(){
		String userName = "test_user";
		HomePiUser resp = homePiUserDAO.findByUserName(userName);
		assertNotNull(resp);
		assertEquals(userName, resp.getUserName());
	}
	
	@Test(expected=NoResultException.class)
	public void testSaveFindDelete() {
		String userName = "HomePiTestUser";
		HomePiUser entity = new HomePiUser();
		entity.setUserName(userName);
		
		homePiUserDAO.save(entity);
		HomePiUser resp = homePiUserDAO.findByUserName(userName);
		assertNotNull(resp);
		assertEquals(userName, resp.getUserName());
		
		homePiUserDAO.delete(resp);
		homePiUserDAO.findByUserName(userName);
	}

	@Test
	public void testUpdate() {
		String userName = "test_user";
		HomePiUser resp = homePiUserDAO.findByUserName(userName);
		assertNotNull(resp);
		assertEquals(userName, resp.getUserName());
		String email = "tester@homepi.com";
		resp.setEmail(email);
		homePiUserDAO.update(resp);
		
		HomePiUser resp2 = homePiUserDAO.findByUserName(userName);
		assertNotNull(resp2);
		assertEquals(email, resp2.getEmail());
	}

	@Test(expected=NoResultException.class)
	public void testSaveFindDeleteById() {
		String userName = "HomePiTestUser";
		HomePiUser entity = new HomePiUser();
		entity.setUserName(userName);
		
		homePiUserDAO.save(entity);
		HomePiUser resp = homePiUserDAO.findByUserName(userName);
		assertNotNull(resp);
		assertEquals(userName, resp.getUserName());
		
		homePiUserDAO.deleteById(resp.getUserId());
		homePiUserDAO.findByUserName(userName);
	}

}
