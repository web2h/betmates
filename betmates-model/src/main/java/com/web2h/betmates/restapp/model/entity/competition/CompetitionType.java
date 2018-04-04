package com.web2h.betmates.restapp.model.entity.competition;

/**
 * Competition type list.
 * 
 * @author web2h
 */
public enum CompetitionType {

	FIFA_WORLD_CUP("FIFA World Cup"),
	NBA_PLAYOFFS("NBA Playoffs"),
	NHL_PLAYOFFS("NHL Playoffs"),
	UEFA_CHAMPIONS_LEAGUE("UEFA Champion's League"),
	UEFA_EURO("UEFA Euro");

	private String value;

	private CompetitionType(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return value;
	}
}