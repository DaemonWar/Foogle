package com.foogle.rest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.foogle.model.manager.TextEntryManager;
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


		for(String query : entry.split("\\s+"))
		{
			keywordList.add(query);System.out.println("keyword: "+query);
		}

		ArrayList<MongoResult> resultList = TextEntryManager.find(keywordList);

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
