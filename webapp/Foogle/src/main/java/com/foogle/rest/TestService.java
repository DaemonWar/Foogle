package com.foogle.rest;

import java.util.ArrayList;
import java.util.LinkedList;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

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
		JSONObject jsonObj = new JSONObject();

		for(String query : entry.split("\\s+"))
		{
			keywordList.add(query);System.out.println("keyword: "+query);
		}

		ArrayList<MongoResult> resultList = TextEntryManager.find(keywordList);

		try {
			jsonObj.put("resultNumber", resultList.size());

			for(int i=0;i<resultList.size();i++)
			{
				JSONObject json = new JSONObject();
				json.put("title",resultList.get(i).getTitle());
				json.put("source",resultList.get(i).getSource());
				json.put("content",resultList.get(i).getContent());

				jsonObj.put("result"+i, json);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return ServiceUtilities.formattedSuccessResponse(jsonObj.toString());
	}
}
