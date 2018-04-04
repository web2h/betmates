package com.web2h.betmates.restapp.persistence.repository.competition;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.web2h.betmates.restapp.model.entity.competition.log.CompetitionLogEvent;

public interface CompetitionLogEventRepository extends CrudRepository<CompetitionLogEvent, Long> {

	List<CompetitionLogEvent> findByCompetition_idOrderByTimestampDesc(Long competitionId);
}
