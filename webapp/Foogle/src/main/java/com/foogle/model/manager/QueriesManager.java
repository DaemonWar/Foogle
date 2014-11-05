package com.foogle.model.manager;

import java.util.ArrayList;

import com.foogle.model.dao.DAOSearchEntries;
import com.foogle.model.entities.SearchEntries;

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
		
		Integer id = dse.find(entry);
		
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
}
