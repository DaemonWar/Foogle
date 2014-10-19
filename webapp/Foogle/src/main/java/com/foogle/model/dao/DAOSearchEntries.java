package com.foogle.model.dao;

import java.util.ArrayList;

import com.foogle.model.entities.SearchEntries;

public class DAOSearchEntries extends DAO<SearchEntries>
{
	public ArrayList<String> findFor(String entry)
	{
		try
		{
			return (ArrayList<String>) em
					.createQuery(
							"SELECT se.query FROM SearchEntries se WHERE se.query LIKE '%"
									+ entry + "%' ORDER BY se.count DESC")
					.setMaxResults(10).getResultList();
		} catch (Exception e)
		{
			System.out.println("LOG FRANCK : " + e.getMessage());
			return null;
		}
	}

}