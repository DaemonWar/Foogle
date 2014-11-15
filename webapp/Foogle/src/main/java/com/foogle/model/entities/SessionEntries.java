package com.foogle.model.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "session_entries")
public class SessionEntries extends AbstractEntity
{
	@Column(name = "user_id", nullable = false)
	private String userId;

	@Column(name = "session_id", nullable = false)
	private String sessionId;

	@ManyToOne(optional=false,fetch=FetchType.EAGER)
	@JoinColumn(name="query_id",nullable=false)
	private SearchEntries queryId;

	public String getUserId()
	{
		return userId;
	}

	public void setUserId(String userId)
	{
		this.userId = userId;
	}

	public String getSessionId()
	{
		return sessionId;
	}

	public void setSessionId(String sessionId)
	{
		this.sessionId = sessionId;
	}

	public SearchEntries getQueryId()
	{
		return queryId;
	}

	public void setQueryId(SearchEntries queryId)
	{
		this.queryId = queryId;
	}
}