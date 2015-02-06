package com.foogle.model.manager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mongodb.morphia.Morphia;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.foogle.model.dao.DAOMongoSingleton;
import com.foogle.model.dao.DAORecommendationEntries;
import com.foogle.model.dao.DAOSearchEntries;
import com.foogle.model.dao.DAOSessionEntries;
import com.foogle.model.dao.DAOTextEntry;
import com.foogle.model.dao.DAOWordEntries;
import com.foogle.model.dao.DAOdwhSingleton;
import com.foogle.model.entities.MatchEntity;
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

	public static JSONObject findInDwhFor(List<String> entryList){
		DAOSearchEntries dse = new DAOSearchEntries(DAOdwhSingleton.openConnection());

		List<String> hebergementCoupe = dse.findSQL("select distinct(country) from team_dim");

		List<Integer> anneeCoupe = dse.findSQL("select year from cup_dim order by year");


		ArrayList<MatchEntity> matchList = new ArrayList<MatchEntity>();

		JSONObject jsonObj = new JSONObject();



		for(String result : entryList){
			logger.info("keyword :: "+result);
		}

		// variables
		int year = 0;
		String country1 = null;
		String country2 = null;

		// affectation des variables
		for(String keyword : entryList){
			if(hebergementCoupe.contains(keyword)){
				if(country1==null){country1 = keyword;} else {country2 = keyword;}
			}
			if(parseInt(keyword)){
				int yearTemp = Integer.parseInt(keyword);
				if(anneeCoupe.contains(yearTemp)){
					year = yearTemp;
				}
			}
		}
		boolean bmatch = false;logger.info(country1+" : "+country2+" : "+year);
		/** SI 1 Pays **/
		// années d'hebergements de la derniere coupe du pays
		if (country1 != null && country2 == null){

			JSONArray jsonArrayTemp = new JSONArray();

			List<Integer> resultList = dse.findSQL("select year from cup_dim where organizer = :organizer", "organizer", country1);

			for(Integer result : resultList){
				jsonArrayTemp.put(result);
			}
			
			JSONObject jSonObj = new JSONObject();
			try {
				jSonObj.put("data", jsonArrayTemp);
				jSonObj.put("header", country1);
				jSonObj.put("title", "Years of hosting");

//				jsonArray.put(jsonObj);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			try {
				jsonObj.put("type", 2);
				jsonObj.put("result", jSonObj);
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
		}

		/** SI 2 Pays et 1 Annee **/
		else if (country1 != null && country2 != null && year != 0){
			logger.info("???????????????????????");

			HashMap<String, Integer> team = new HashMap<String, Integer>();

			//ID pays 1
			List<Integer> resultList1 = dse.findSQL("select sid_team from team_dim where country = :country and (date_from, date_to) OVERLAPS ('"+year+"-01-01', '"+year+"-12-30')", "country", country1);
			team.put(country1, resultList1.get(0));
			//ID pays 2
			List<Integer> resultList2 = dse.findSQL("select sid_team from team_dim where country = :country and (date_from, date_to) OVERLAPS ('"+year+"-01-01', '"+year+"-12-30')", "country", country2);
			team.put(country2, resultList2.get(0));


			//recuperation des matches
			List<Object[]> resultList3 = dse.findSQL("select  date_id,total_goals_in_favor, goal_difference, sid_stage from team_in_match_fact where sid_team = :sid_team and sid_opponent = :sid_opponent", "sid_team", team.get(country1), "sid_opponent", team.get(country2) );

			for(Object[] result : resultList3){

				int date_id = Integer.parseInt(result[0].toString());
				int score1 = Integer.parseInt(result[1].toString());
				int score2 = score1 - Integer.parseInt(result[2].toString());
				int sid_stage = Integer.parseInt(result[3].toString());
				List<String> resultList5 = dse.findSQL("select name from stage_dim where sid_stage = "+sid_stage);
				String stage = resultList5.get(0);

				List<Object[]> resultList4 = dse.findSQL("select year,date from date_dim where date_id = "+date_id);
				int year2 = Integer.parseInt(resultList4.get(0)[0].toString());
				String date = resultList4.get(0)[1].toString();

				matchList.add(new MatchEntity(country1, country2, score1, score2, year2, date, stage));    
				bmatch = true;
			}
			//JSON
			JSONArray jsonArrayTemp = new JSONArray();
	
			for(MatchEntity match : matchList){
				if(match.year == year){
					try {
						JSONObject jSonObj = new JSONObject();
						jSonObj.put("country1", match.country1);
						jSonObj.put("score1", match.score1);
						jSonObj.put("country2", match.country2);
						jSonObj.put("score2", match.score2);
						jSonObj.put("date", match.date);
						jSonObj.put("stage", match.stage);

						jsonArrayTemp.put(jSonObj);
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}try {
				jsonObj.put("type", 1);
				jsonObj.put("result", jsonArrayTemp);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if(country1 != null && country2 != null && (year == 0 || (year != 0 && bmatch == false))){
			logger.info("!!!!!!!!!!!!!!!!!!!!!!");
			matchList.clear();

			HashMap<Integer,Integer> team1 = new HashMap<Integer,Integer>();
			HashMap<Integer,Integer> team2 = new HashMap<Integer,Integer>();

			for (Integer yearWC : anneeCoupe){
				//ID pays 1
				List<Integer> resultList1 = dse.findSQL("select sid_team from team_dim where country = :country and (date_from, date_to) OVERLAPS ('"+yearWC+"-01-01', '"+yearWC+"-12-30')", "country", country1);
				if( ! resultList1.isEmpty())
					team1.put(yearWC, Integer.parseInt(resultList1.get(0).toString()));
				//ID pays 2
				List<Integer> resultList2 = dse.findSQL("select sid_team from team_dim where country = :country and (date_from, date_to) OVERLAPS ('"+yearWC+"-01-01', '"+yearWC+"-12-30')", "country", country2);
				if( ! resultList2.isEmpty())
					team2.put(yearWC, Integer.parseInt(resultList2.get(0).toString()));
			}
			logger.info("team 1 2002 ="+team1.get(2002));logger.info("team 2 2002 ="+team2.get(2002));

			for (Integer yearWC : anneeCoupe){
				//recuperation des matches
				List<Object[]> resultList3 = dse.findSQL("select date_id,total_goals_in_favor, goal_difference, sid_stage from team_in_match_fact where sid_team = "+team1.get(yearWC)+" and sid_opponent = "+team2.get(yearWC));
				logger.info(yearWC+" : "+resultList3.size());
				if( ! resultList3.isEmpty()){
					for(Object[] result : resultList3){

						int date_id = Integer.parseInt(result[0].toString());
						int score1 = Integer.parseInt(result[1].toString());
						int score2 = score1 - Integer.parseInt(result[2].toString());
						int sid_stage = Integer.parseInt(result[3].toString());
						List<String> resultList5 = dse.findSQL("select name from stage_dim where sid_stage = "+sid_stage);
						String stage = resultList5.get(0);

						List<Object[]> resultList4 = dse.findSQL("select year,date from date_dim where date_id = "+date_id);
						int year2 = Integer.parseInt(resultList4.get(0)[0].toString());
						String date = resultList4.get(0)[1].toString();
						logger.info("result : "+country1);
						logger.info("result : "+country2);
						logger.info("result : "+score1);
						logger.info("result : "+score2);
						logger.info("result : "+year2);
						logger.info("result : "+date);
						matchList.add(new MatchEntity(country1, country2, score1, score2, year2, date, stage));    logger.info("SIZE "+matchList.size());
					}
				}
			}
			//JSON
			JSONArray jsonArrayTemp = new JSONArray();

			for(MatchEntity match : matchList){
				try {
					JSONObject jSonObj = new JSONObject();
					jSonObj.put("country1", match.country1);
					jSonObj.put("score1", match.score1);
					jSonObj.put("country2", match.country2);
					jSonObj.put("score2", match.score2);
					jSonObj.put("date", match.date);
					jSonObj.put("stage", match.stage);

					jsonArrayTemp.put(jSonObj);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}try {
				jsonObj.put("result", jsonArrayTemp);
				jsonObj.put("type", 1);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}



		return jsonObj;

		//        for(String result : resultList){
		//            logger.info("result :: "+result);
		//        }
	}

	private static boolean parseInt(String keyword){
		try{
			Integer.parseInt(keyword);
		} catch(NumberFormatException e){
			return false;
		}
		return true;
	}
}
