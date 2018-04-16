package com.web2h.betmates.restapp.persistence.repository.reference;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.web2h.betmates.restapp.model.entity.reference.Reference;

/**
 * Common reference repository interface.
 * 
 * @author web2h
 */
public interface ReferenceRepository<R extends Reference> extends CrudRepository<R, Serializable> {

	@Query("SELECT COUNT(r) FROM Reference r WHERE r.id IN :referenceIds")
	int countByReferenceIds(@Param("referenceIds") List<Long> referenceIds);

	/**
	 * Retrieves a reference by its English name
	 * 
	 * @param nameEn
	 *            The English name to look for
	 * @return The retrieved reference, null if none could be found
	 */
	R findByNameEn(String nameEn);

	/**
	 * Retrieves a reference by its French name
	 * 
	 * @param nameFr
	 *            The French name to look for
	 * @return The retrieved reference, null if none could be found
	 */
	R findByNameFr(String nameFr);
}