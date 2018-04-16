package com.web2h.betmates.restapp.persistence.repository.reference;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.junit4.SpringRunner;

import com.web2h.betmates.restapp.persistence.repository.user.AppUserRepository;

@RunWith(SpringRunner.class)
@DataJpaTest
public class TeamRepositoryTest {

	@Autowired
	private TeamRepository sut;

	@Autowired
	private AppUserRepository appUserRepository;

	@Test
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:test-dataset/data.sql")
	public void countByReferenceIds_AllTeamsExists_ReturnTheRightTeamCount() {

		List<Long> teamIds = new ArrayList<>();
		teamIds.add(31l);
		teamIds.add(32l);
		teamIds.add(33l);
		teamIds.add(34l);

		int teamCount = sut.countByReferenceIds(teamIds);
		Assert.assertEquals(teamIds.size(), teamCount);
	}

	@Test
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:test-dataset/data.sql")
	public void countByReferenceIds_HalfOfTeamsExists_ReturnTheRightTeamCount() {

		List<Long> teamIds = new ArrayList<>();
		teamIds.add(31l);
		teamIds.add(32l);
		teamIds.add(999l);
		teamIds.add(1000l);

		int teamCount = sut.countByReferenceIds(teamIds);
		Assert.assertEquals(2, teamCount);
	}

	@Test
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:test-dataset/data.sql")
	public void countByReferenceIds_NoneOfTeamsExists_ReturnTheRightTeamCount() {

		List<Long> teamIds = new ArrayList<>();
		teamIds.add(997l);
		teamIds.add(998l);
		teamIds.add(999l);
		teamIds.add(1000l);

		int teamCount = sut.countByReferenceIds(teamIds);
		Assert.assertEquals(0, teamCount);
	}
}