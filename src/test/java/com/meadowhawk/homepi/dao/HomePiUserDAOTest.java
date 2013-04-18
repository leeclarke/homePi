package com.meadowhawk.homepi.dao;

import static org.junit.Assert.*;

import java.util.List;

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
		fail("Not yet implemented");
	}

	@Test
	public void testFindAll() {
		List<HomePiUser> resp = homePiUserDAO.findAll();
		assertNotNull(resp);
	}

	@Test
	public void testFindByUserName(){
		String userName = "test_user";
		HomePiUser resp = homePiUserDAO.findByUserName(userName);
		assertNotNull(resp);
		assertEquals(userName, resp.getUserName());
	}
	
	@Test
	public void testSave() {
		String userName = "TestUser";
		HomePiUser entity = new HomePiUser();
		entity.setUserName(userName);
		
		homePiUserDAO.save(entity);
	}

	@Test
	public void testUpdate() {
		fail("Not yet implemented");
	}

	@Test
	public void testDelete() {
		fail("Not yet implemented");
	}

	@Test
	public void testDeleteById() {
		fail("Not yet implemented");
	}

}
