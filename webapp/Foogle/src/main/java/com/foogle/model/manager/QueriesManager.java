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
		// TODO Algo de recommandation
		
		ArrayList<String> returnObject = new ArrayList<String>();

		returnObject.add("World Cup 2014");
		returnObject.add("World Cup 2008");
		returnObject.add("Zinedine Zidane");
		returnObject.add("Fifa rules");
		
		return returnObject;
	}
}
