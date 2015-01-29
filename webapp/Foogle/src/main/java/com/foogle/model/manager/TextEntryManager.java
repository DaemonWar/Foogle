package com.foogle.model.manager;

import java.util.ArrayList;
import java.util.Collections;

import org.mongodb.morphia.Morphia;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.foogle.model.dao.DAOMongoSingleton;
import com.foogle.model.dao.DAOTextEntry;
import com.foogle.model.entities.TextEntry;
import com.foogle.rest.utils.MongoResult;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

public class TextEntryManager {

	private final static Logger logger = LoggerFactory.getLogger(TextEntryManager.class);
	private static DAOTextEntry daoTextEntry = null;

	public static ArrayList<MongoResult> find(ArrayList<String> keywordList){
		StringBuilder sb = new StringBuilder();
		for(String keyword : keywordList){
			sb.append(keyword);
			sb.append(" ");
		}
		daoTextEntry = getDao();
		DBCollection coll = daoTextEntry.getCollection();

		// Find using the text index
		BasicDBObject search = new BasicDBObject("$search", sb.toString());
		BasicDBObject textSearch = new BasicDBObject("$text", search);
		int matchCount = coll.find(textSearch).count();

		// Find the highest scoring match
		BasicDBObject projection = new BasicDBObject("score", new BasicDBObject("$meta", "textScore"));

		// cast en obj MongoResult
		ArrayList<MongoResult> mongoResultList = new ArrayList<MongoResult>();
		for(DBObject obj : coll.find(textSearch, projection)){
			mongoResultList.add(new MongoResult(obj.get("title").toString(), obj.get("source").toString(), obj.get("content").toString(), obj.get("score").toString()));
		}
		
		// tri
		Collections.sort(mongoResultList, Collections.reverseOrder());
		
		// affichage
		for(MongoResult result : mongoResultList){
			System.out.println(result.getTitle()+" :: "+result.getScoring());

		}
		return mongoResultList;
	}


	private static DAOTextEntry getDao() {
		if (daoTextEntry == null) {
			try {
				MongoClient mongo = DAOMongoSingleton.getMongo();
				Morphia morphia = new Morphia();
				morphia.map(TextEntry.class);
				daoTextEntry = new DAOTextEntry(morphia, mongo);
				logger.debug("new DAOTextEntry created");
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		}
		return daoTextEntry;
	}

}
