package com.foogle.model.dao;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import javax.persistence.EntityManager;

public abstract class DAO<T>
{
	protected EntityManager em;

	private Class<T> type;

	@SuppressWarnings("unchecked")
	public DAO()
	{
		Type t = getClass().getGenericSuperclass();
		ParameterizedType pt = (ParameterizedType) t;
		type = (Class) pt.getActualTypeArguments()[0];

		this.em = DAOSingleton.openConnection();
	}

	public DAO(EntityManager em)
	{
		Type t = getClass().getGenericSuperclass();
		ParameterizedType pt = (ParameterizedType) t;
		type = (Class) pt.getActualTypeArguments()[0];

		this.em = em;
	}

	public T find(final int id)
	{
		return (T) this.em.find(type, id);
	}

	public boolean create(final T obj)
	{
		try
		{
			try
			{
				em.getTransaction().begin();
				em.persist(obj);
				em.getTransaction().commit();
			} catch (Exception e)
			{
				e.printStackTrace();
				return false;
			} finally
			{
				if (em.getTransaction().isActive())
					em.getTransaction().rollback();
			}
		} catch (Exception e)
		{
			return false;
		}
		return true;
	}

	public boolean update(T obj)
	{
		try
		{
			em.getTransaction().begin();
			em.merge(obj);
			em.getTransaction().commit();
		} catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public boolean delete(T obj)
	{
		try
		{
			em.getTransaction().begin();
			em.remove(obj);
			em.getTransaction().commit();

		} catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}
}