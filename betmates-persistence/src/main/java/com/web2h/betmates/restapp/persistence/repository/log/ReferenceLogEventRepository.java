package com.web2h.betmates.restapp.persistence.repository.log;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.web2h.betmates.restapp.model.entity.reference.log.ReferenceLogEvent;

/**
 * Reference log event repository interface.
 * 
 * @author web2h
 */
public interface ReferenceLogEventRepository extends CrudRepository<ReferenceLogEvent, Long> {

	List<ReferenceLogEvent> findByReference_idOrderByTimestampDesc(Long referenceId);
}