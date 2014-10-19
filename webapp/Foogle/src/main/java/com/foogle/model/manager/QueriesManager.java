package com.foogle.model.manager;

import java.util.ArrayList;

import com.foogle.model.dao.DAOSearchEntries;

public class QueriesManager
{
	public static ArrayList<String> findQueriesFor(String entry)
	{
		DAOSearchEntries dse = new DAOSearchEntries();
		
		return dse.findFor(entry);
	}
}
