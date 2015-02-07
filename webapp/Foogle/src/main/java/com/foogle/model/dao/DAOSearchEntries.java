package com.foogle.model.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.foogle.model.entities.SearchEntries;

public class DAOSearchEntries extends DAO<SearchEntries>
{
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	public DAOSearchEntries (){}
	
	public DAOSearchEntries (EntityManager em){
		super(em);
	}
	
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

	public Integer findId(String entry)
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

	public SearchEntries findObject(String entry)
	{
		try
		{
			return (SearchEntries) em.createQuery(
					"SELECT se FROM SearchEntries se WHERE se.query LIKE '"
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

	public  List<SearchEntries> getCommonEntries(int maxResult)
	{
		try
		{
			return ( List<SearchEntries>) em
					.createQuery(
							"SELECT se FROM SearchEntries se ORDER BY se.count DESC")
					.setMaxResults(maxResult).getResultList();
		} catch (Exception e)
		{
			logger.info(e.getMessage());
			return new ArrayList<SearchEntries>();
		}
	}
	
	public List findSQL(String sql, Object... parameters) {
		  try{
		   Query q = em.createNativeQuery(sql);//"select id from users where username = :username"
		   
		   for (int i=0;i<parameters.length;i+=2){
		    q.setParameter((String)parameters[i], parameters[i+1]);//q.setParameter("username", "lt");
		   }
		   
		   List values = q.getResultList();
		   return values;
		  }catch (Exception e) {
		   return null;
		  }
		 }
}