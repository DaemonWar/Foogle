package com.foogle.rest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.foogle.model.manager.QueriesManager;
import com.foogle.rest.utils.MongoResult;
import com.foogle.rest.utils.ServiceUtilities;

@Path("/find")
public class TestService {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@GET
	@Produces("application/json")
	@Path("/{query}")
	public Response findQueriesFor(@PathParam("query") String entry)
	{
		ArrayList<String> keywordList = new ArrayList<String>();
		JSONArray jsonList = new JSONArray();

		// TODO Lucene here
		
		
		String result = QueriesManager.lucene(entry);
		logger.info("Lucene result: "+result);
		
		
		// DEPRECATED 
		for(String query : entry.split("\\s+"))
		{
			keywordList.add(query);
		}

		ArrayList<MongoResult> resultList = QueriesManager.findMongoResult(result);

		for(MongoResult mResult : resultList)
		{
			Map<String, String> m = new HashMap<String, String>();
			m.put("title", mResult.getTitle());
			m.put("source", mResult.getSource());
			m.put("content", mResult.getContent());

			jsonList.put(m);
		}

		return ServiceUtilities.formattedSuccessResponse(jsonList.toString());
	}
}
