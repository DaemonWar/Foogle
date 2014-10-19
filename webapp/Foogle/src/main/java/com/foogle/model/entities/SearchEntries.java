package com.foogle.model.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "search_entries")
public class SearchEntries extends AbstractEntity
{
	@Column(name = "query")
	private String query;

	@Column(name = "count")
	private String count;

	public String getQuery()
	{
		return query;
	}

	public void setQuery(String query)
	{
		this.query = query;
	}

	public String getCount()
	{
		return count;
	}

	public void setCount(String count)
	{
		this.count = count;
	}
}