package com.foogle.model.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.foogle.model.entities.WordEntries;

public class DAOWordEntries extends DAO<WordEntries>
{
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	public WordEntries find(String word)
	{
		WordEntries entry = null;
		
		try
		{
			entry = (WordEntries) em.createQuery(
					"SELECT we FROM WordEntries we WHERE we.word LIKE '" + word + "'").getSingleResult();

		} catch (Exception e)
		{
			logger.info(e.getMessage());
			
			entry = new WordEntries();
			entry.setWord(word);

			this.create(entry);
		}
		
		return entry;
	}
}