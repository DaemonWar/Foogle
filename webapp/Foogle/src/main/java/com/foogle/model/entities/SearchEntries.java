package com.foogle.model.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "search_entries")
public class SearchEntries extends AbstractEntity
{
	@Column(name = "query", nullable = false)
	private String query;

	@Column(name = "count", nullable = false, columnDefinition = "int default 1")
	private int count = 1;

	public String getQuery()
	{
		return query;
	}

	public void setQuery(String query)
	{
		this.query = query;
	}

	public int getCount()
	{
		return count;
	}

	public void setCount(int count)
	{
		this.count = count;
	}
}