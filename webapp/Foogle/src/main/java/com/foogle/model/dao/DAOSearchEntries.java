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
			return null;
		}
	}

	public Integer find(String entry)
	{
		try
		{
			return (Integer) em.createQuery(
					"SELECT se.id FROM SearchEntries se WHERE se.query LIKE '"
							+ entry + "'").getSingleResult();
		} catch (Exception e)
		{
			return null;
		}
	}

	public void increment(Integer id)
	{
		try
		{
			SearchEntries se = find(id);
			
			se.setCount(se.getCount() + 1);
			
			update(se);
		} catch (Exception e)
		{
			System.out.println(e.getMessage());
		}
	}
}