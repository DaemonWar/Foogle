package com.foogle.model.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "recommendation_entries")
public class RecommendationEntries extends AbstractEntity
{
	@Column(name = "session_id", nullable = false)
	private long sessionId;

	@ManyToOne(optional=false,fetch=FetchType.EAGER)
	@JoinColumn(name="term_id",nullable=false)
	private WordEntries term;

	@Column(name = "count", nullable = false)
	private Float count;

	public long getSessionId()
	{
		return sessionId;
	}

	public void setSessionId(long sessionId)
	{
		this.sessionId = sessionId;
	}

	public WordEntries getTerm()
	{
		return term;
	}

	public void setTerm(WordEntries term)
	{
		this.term = term;
	}

	public Float getCount()
	{
		return count;
	}

	public void setCount(Float count)
	{
		this.count = count;
	}
}