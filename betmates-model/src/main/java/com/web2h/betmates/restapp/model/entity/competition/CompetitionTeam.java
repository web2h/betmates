package com.web2h.betmates.restapp.model.entity.competition;

import static com.web2h.betmates.restapp.model.entity.FieldLength.GROUP_MAX_LENGTH;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.web2h.betmates.restapp.model.entity.reference.Team;

/**
 * Competition team entity class.
 * 
 * @author web2h
 */
@Entity
@Table(name = "competition_teams")
public class CompetitionTeam {

	/** ID - Internal ID. */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	protected Long id;

	@ManyToOne
	@JoinColumn(name = "competition_id")
	private Competition competition;

	@ManyToOne
	@JoinColumn(name = "team_id")
	private Team team;

	@Enumerated(EnumType.STRING)
	@Column(name = "competition_group", length = GROUP_MAX_LENGTH)
	private CompetitionGroup group;

	@Column(name = "position")
	private Integer position;

	@Override
	public int hashCode() {
		HashCodeBuilder hcb = new HashCodeBuilder();
		hcb.append(competition);
		hcb.append(team);
		return hcb.toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof CompetitionTeam)) {
			return false;
		}
		CompetitionTeam that = (CompetitionTeam) obj;
		EqualsBuilder eb = new EqualsBuilder();
		eb.append(competition, that.competition);
		eb.append(team, that.team);
		return eb.isEquals();
	}

	public Competition getCompetition() {
		return competition;
	}

	public void setCompetition(Competition competition) {
		this.competition = competition;
	}

	public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}

	public CompetitionGroup getGroup() {
		return group;
	}

	public void setGroup(CompetitionGroup group) {
		this.group = group;
	}

	public Integer getPosition() {
		return position;
	}

	public void setPosition(Integer position) {
		this.position = position;
	}
}