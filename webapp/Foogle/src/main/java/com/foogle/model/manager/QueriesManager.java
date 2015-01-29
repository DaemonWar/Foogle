package com.foogle.model.manager;

import java.util.ArrayList;

import com.foogle.model.dao.DAOSearchEntries;
import com.foogle.model.dao.DAOSessionEntries;
import com.foogle.model.entities.SearchEntries;
import com.foogle.model.entities.SessionEntries;

public class QueriesManager
{
	public static ArrayList<String> findQueriesFor(String entry)
	{
		DAOSearchEntries dse = new DAOSearchEntries();
		
		return dse.findFor(entry);
	}

	public static void putEntry(String entry)
	{
		DAOSearchEntries dse = new DAOSearchEntries();
		
		Integer id = dse.findId(entry);
		
		if(id == null)
		{
			SearchEntries se = new SearchEntries();
			se.setQuery(entry);
			
			dse.create(se);
		} else
		{
			dse.increment(id);
		}
	}

	public static void putSessionEntry(String entry, String user, String session)
	{
		DAOSearchEntries dse = new DAOSearchEntries();
		
		SearchEntries query = dse.findObject(entry);
		
		DAOSessionEntries dsse = new DAOSessionEntries();
		
		SessionEntries sessionEntry = new SessionEntries();
		
		sessionEntry.setQueryId(query);
		sessionEntry.setUserId(user);
		sessionEntry.setSessionId(session);
		
		dsse.create(sessionEntry);
	}

	public static ArrayList<String> findRecommendationsFor(String entry)
	{
		int maxResult = 4;
		// TODO Algo de recommandation
		
		ArrayList<String> returnObject = null;
		
		DAOSearchEntries dse = new DAOSearchEntries();
		DAOSessionEntries dsse = new DAOSessionEntries();
		
		Integer entryId = dse.findId(entry);
		
		//Trouver si la requête a déjà été faite
		if (entryId == null)
		{
			//Si oui, trouver...
			ArrayList<SearchEntries> entries = dsse.getRecommandedEntries(entryId, maxResult);
			
			if (entries == null)
			{
				returnObject = dse.getCommonEntries(maxResult);
			} else
			{
				returnObject = new ArrayList<String>();
				
				for (SearchEntries searchEntries : entries)
				{
					returnObject.add(searchEntries.getQuery());
				}
			}
		} else
		{
			//Sinon, les X requêtes les plus faites
			returnObject = dse.getCommonEntries(maxResult);
		}
		
		return returnObject;
	}
}
