package com.foogle.model.dao;

import java.util.ArrayList;

import com.foogle.model.entities.SearchEntries;
import com.foogle.model.entities.SessionEntries;

public class DAOSessionEntries extends DAO<SessionEntries>
{
	public ArrayList<SearchEntries> getRecommandedEntries(Integer entryId, int maxResult)
	{
		try
		{
			return (ArrayList<SearchEntries>) em
					.createQuery(
							"SELECT DISTINCT se.queryId FROM sessionEntries se WHERE se.queryId.id <> " + entryId
									+ " AND se.sessionId IN (SELECT se2.sessionId FROM sessionEntries se2 WHERE se2.queryId.id = " + entryId
									+ ") ORDER BY se.queryId.count DESC").setMaxResults(maxResult).getResultList();
		} catch (Exception e)
		{
			return null;
		}
	}

}