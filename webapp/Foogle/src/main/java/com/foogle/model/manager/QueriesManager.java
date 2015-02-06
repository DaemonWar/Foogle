package com.foogle.model.manager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.mongodb.morphia.Morphia;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.foogle.model.dao.DAOMongoSingleton;
import com.foogle.model.dao.DAORecommendationEntries;
import com.foogle.model.dao.DAOSearchEntries;
import com.foogle.model.dao.DAOSessionEntries;
import com.foogle.model.dao.DAOTextEntry;
import com.foogle.model.dao.DAOWordEntries;
import com.foogle.model.entities.SearchEntries;
import com.foogle.model.entities.SessionEntries;
import com.foogle.model.entities.TextEntry;
import com.foogle.model.entities.WordEntries;
import com.foogle.rest.utils.LuceneAndMahoutUtilities;
import com.foogle.rest.utils.MongoResult;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

public class QueriesManager
{
	private final static Logger logger = LoggerFactory.getLogger(QueriesManager.class);

	public static ArrayList<String> findQueriesFor(String entry)
	{
		DAOSearchEntries dse = new DAOSearchEntries();

		return dse.findFor(entry);
	}

	public static void putEntry(String entry)
	{
		DAOSearchEntries dse = new DAOSearchEntries();

		Integer id = dse.findId(entry);

		if (id == null)
		{
			SearchEntries se = new SearchEntries();
			se.setQuery(entry);

			dse.create(se);
		} else
		{
			dse.increment(id);
		}
	}

	public static void putRecommandation(String sessionId, String term)
	{
		DAORecommendationEntries dre = new DAORecommendationEntries();

		DAOWordEntries dwe = new DAOWordEntries();

		WordEntries entry = dwe.find(term);

		dre.addTerm(Long.valueOf(sessionId.substring(2)), entry);
	}

	public static void putSessionEntry(String entry, String user, String session)
	{
		DAOSearchEntries dse = new DAOSearchEntries();

		SearchEntries query = dse.findObject(entry);

		DAOSessionEntries dsse = new DAOSessionEntries();

		SessionEntries sessionEntry = new SessionEntries();

		sessionEntry.setQueryId(query);
		sessionEntry.setUserId(user);
		sessionEntry.setSessionId(session);

		dsse.create(sessionEntry);
	}

	public static List<RecommendedItem> findRecommendationsFor(String sessionId)
	{
		try
		{
			int maxResult = 10;

			LuceneAndMahoutUtilities utils = LuceneAndMahoutUtilities.getInstance();

			Recommender recommender = utils.getRecommender();

			return recommender.recommend(Long.valueOf(sessionId.substring(2)), maxResult);
		} catch (Exception e)
		{
			logger.info(e.getMessage());

			return null;
		}
		/*
		 * for (RecommendedItem recommendedItem : recommendedItems) {
		 * logger.info("Item: " + recommendedItem.getItemID());
		 * logger.info(" (Value: " + recommendedItem.getValue() + "/5)"); }
		 */
		/*
		 * ArrayList<String> returnObject = null;
		 * 
		 * DAOSearchEntries dse = new DAOSearchEntries(); DAOSessionEntries dsse
		 * = new DAOSessionEntries();
		 * 
		 * Integer entryId = dse.findId(entry);
		 * 
		 * //Trouver si la requête a déjà été faite if (entryId == null) { //Si
		 * oui, trouver... ArrayList<SearchEntries> entries =
		 * dsse.getRecommandedEntries(entryId, maxResult);
		 * 
		 * if (entries == null) { returnObject =
		 * dse.getCommonEntries(maxResult); } else { returnObject = new
		 * ArrayList<String>();
		 * 
		 * for (SearchEntries searchEntries : entries) {
		 * returnObject.add(searchEntries.getQuery()); } } } else { //Sinon, les
		 * X requêtes les plus faites returnObject =
		 * dse.getCommonEntries(maxResult); }
		 * 
		 * return returnObject;
		 */
	}

	public static ArrayList<MongoResult> findMongoResult(String entry)
	{
		// DAO
		MongoClient mongo = DAOMongoSingleton.getMongo();
		Morphia morphia = new Morphia().map(TextEntry.class);
		DAOTextEntry daoTextEntry = new DAOTextEntry(morphia, mongo);
		DBCollection coll = daoTextEntry.getCollection();

		// Find using the text index
		BasicDBObject search = new BasicDBObject("$search", entry);
		BasicDBObject textSearch = new BasicDBObject("$text", search);

		// Find the highest scoring match
		BasicDBObject projection = new BasicDBObject("score", new BasicDBObject("$meta", "textScore"));

		// Cast en obj MongoResult
		ArrayList<MongoResult> mongoResultList = new ArrayList<MongoResult>();
		for (DBObject obj : coll.find(textSearch, projection))
		{
			mongoResultList.add(new MongoResult(obj.get("title").toString(), obj.get("source").toString(), obj.get("content").toString(), obj.get("score")
					.toString()));
		}

		// Tri par score decroissant
		Collections.sort(mongoResultList, Collections.reverseOrder());

		// affichage
		for (MongoResult result : mongoResultList)
		{
			System.out.println(result.getTitle() + " :: " + result.getScoring());

		}
		return mongoResultList;
	}
}
