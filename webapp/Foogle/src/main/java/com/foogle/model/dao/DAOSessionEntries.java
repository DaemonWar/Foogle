package com.foogle.model.dao;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.foogle.model.entities.SearchEntries;
import com.foogle.model.entities.SessionEntries;

public class DAOSessionEntries extends DAO<SessionEntries>
{
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	public List<SearchEntries> getRecommandedEntries(Integer entryId, int maxResult)
	{
		try
		{
			return (List<SearchEntries>) em
					.createQuery(
							"SELECT 1, se.queryId.query, se.queryId.count FROM SessionEntries se WHERE se.queryId.id <> " + entryId
									+ " AND se.sessionId IN (SELECT se2.sessionId FROM SessionEntries se2 WHERE se2.queryId.id = " + entryId
									+ ") GROUP BY se.queryId.query ORDER BY se.queryId.count DESC").setMaxResults(maxResult).getResultList();
		} catch (Exception e)
		{
			logger.info(e.getMessage());
			return new ArrayList<SearchEntries>();
		}
	}

}