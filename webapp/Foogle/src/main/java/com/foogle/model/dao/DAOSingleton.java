package com.foogle.model.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class DAOSingleton
{
	private static EntityManagerFactory emf = Persistence
			.createEntityManagerFactory("managerLocal");
	private static EntityManager em;

	private DAOSingleton()
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
			new DAOSingleton();
		}
		return em;
	}
}