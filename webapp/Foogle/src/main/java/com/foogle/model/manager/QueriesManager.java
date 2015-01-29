package com.foogle.model.manager;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.util.Version;
import org.mongodb.morphia.Morphia;

import com.foogle.model.dao.DAOMongoSingleton;
import com.foogle.model.dao.DAOSearchEntries;
import com.foogle.model.dao.DAOSessionEntries;
import com.foogle.model.dao.DAOTextEntry;
import com.foogle.model.entities.SearchEntries;
import com.foogle.model.entities.SessionEntries;
import com.foogle.model.entities.TextEntry;
import com.foogle.rest.utils.MongoResult;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

public class QueriesManager
{
	public static ArrayList<String> findQueriesFor(String entry)
	{
		DAOSearchEntries dse = new DAOSearchEntries();
		
		return dse.findFor(entry);
	}

	public static void putEntry(String entry)
	{
		DAOSearchEntries dse = new DAOSearchEntries();
		
		Integer id = dse.findId(entry);
		
		if(id == null)
		{
			SearchEntries se = new SearchEntries();
			se.setQuery(entry);
			
			dse.create(se);
		} else
		{
			dse.increment(id);
		}
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

	public static ArrayList<String> findRecommendationsFor(String entry)
	{
		int maxResult = 4;
		// TODO Algo de recommandation
		
		ArrayList<String> returnObject = null;
		
		DAOSearchEntries dse = new DAOSearchEntries();
		DAOSessionEntries dsse = new DAOSessionEntries();
		
		Integer entryId = dse.findId(entry);
		
		//Trouver si la requête a déjà été faite
		if (entryId == null)
		{
			//Si oui, trouver...
			ArrayList<SearchEntries> entries = dsse.getRecommandedEntries(entryId, maxResult);
			
			if (entries == null)
			{
				returnObject = dse.getCommonEntries(maxResult);
			} else
			{
				returnObject = new ArrayList<String>();
				
				for (SearchEntries searchEntries : entries)
				{
					returnObject.add(searchEntries.getQuery());
				}
			}
		} else
		{
			//Sinon, les X requêtes les plus faites
			returnObject = dse.getCommonEntries(maxResult);
		}
		
		return returnObject;
	}
	
	public static ArrayList<MongoResult> findMongoResult(String entry){

		// Transformez la liste de keywords en un string
//		StringBuilder sb = new StringBuilder();
//		for(String keyword : keywordList){
//			sb.append(keyword);
//			sb.append(" ");
//		}
//		entry = sb.toString() 
		
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
		for(DBObject obj : coll.find(textSearch, projection)){
			mongoResultList.add(new MongoResult(obj.get("title").toString(), obj.get("source").toString(), obj.get("content").toString(), obj.get("score").toString()));
		}
		
		// Tri par score decroissant
		Collections.sort(mongoResultList, Collections.reverseOrder());
		
		// affichage
		for(MongoResult result : mongoResultList){
			System.out.println(result.getTitle()+" :: "+result.getScoring());

		}
		return mongoResultList;
	}
	
	public static String lucene(String entry){
		
        TokenStream tokenStream = new StandardTokenizer(Version.LUCENE_36,new StringReader(entry));
        StringBuilder sb = new StringBuilder();
        tokenStream = new org.apache.lucene.analysis.core.StopFilter(Version.LUCENE_36, tokenStream, StandardAnalyzer.STOP_WORDS_SET);
        CharTermAttribute token = tokenStream.getAttribute(CharTermAttribute.class);
        try {
        	tokenStream.reset();
			while (tokenStream.incrementToken()) 
			{
			    if (sb.length() > 0) 
			    {
			        sb.append(" ");
			    }
			    sb.append(token.toString());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return sb.toString();
	}

}
