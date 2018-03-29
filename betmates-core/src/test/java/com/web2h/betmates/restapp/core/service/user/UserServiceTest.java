package com.web2h.betmates.restapp.core.service.user;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.web2h.betmates.restapp.core.service.user.UserService;
import com.web2h.betmates.restapp.model.exception.AlreadyExistsException;

/**
 * User service test class.
 * 
 * @author web2h
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class UserServiceTest {

	@Autowired
	private UserService sut;

	@Test(expected = NullPointerException.class)
	public void signUpAppUser_WithNullAppUser_ThrowNullPointerException() throws AlreadyExistsException {
		sut.signUpAppUser(null);
	}

	// TODO Complete tests
}