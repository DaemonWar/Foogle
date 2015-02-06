package com.foogle.model.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.foogle.model.entities.RecommendationEntries;
import com.foogle.model.entities.WordEntries;

public class DAORecommendationEntries extends DAO<RecommendationEntries>
{
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	public void addTerm(long sessionId, WordEntries term)
	{
		RecommendationEntries entry = null;
		
		try
		{
			entry = (RecommendationEntries) em.createQuery(
					"SELECT re FROM RecommendationEntries re WHERE re.sessionId = " + sessionId + " AND re.term.word LIKE '" + term.getWord() + "'").getSingleResult();

			entry.setCount(entry.getCount() + 1);

			this.update(entry);

		} catch (Exception e)
		{
			logger.info(e.getMessage());
			
			entry = new RecommendationEntries();
			entry.setSessionId(sessionId);
			entry.setTerm(term);
			entry.setCount((float) 1);

			this.create(entry);
		}
	}
}