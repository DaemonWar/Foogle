package com.foogle.model.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class DAOdwhSingleton 
{
	private static EntityManagerFactory emf = Persistence
			.createEntityManagerFactory("PersistenceUnit");
	private static EntityManager em;

	private DAOdwhSingleton()
	{
		try
		{
			em = emf.createEntityManager();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static EntityManager openConnection()
	{
		if (em == null)
		{
			new DAOdwhSingleton();
		}
		return em;
	}
}
