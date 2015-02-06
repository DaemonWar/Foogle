package com.foogle.rest;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.foogle.model.manager.QueriesManager;
import com.foogle.rest.utils.LuceneAndMahoutUtilities;
import com.foogle.rest.utils.ServiceUtilities;

@Path("/query")
public class QueryService
{
	private final Logger logger = LoggerFactory.getLogger(getClass());

	@GET
	@Path("/autocomplete/{query}")
	public Response findQueriesFor(@PathParam("query") String entry)
	{
		JSONArray json = new JSONArray();

		ArrayList<String> queries = QueriesManager.findQueriesFor(entry);

		for (String query : queries)
		{
			json.put(query);
		}

		return ServiceUtilities.formattedSuccessResponse(json.toString());
	}

	@GET
	@Path("/save/{query}/{user}/{session}")
	public Response save(@PathParam("query") String entry, @PathParam("user") String user, @PathParam("session") String session)
	{
		try
		{
			QueriesManager.putEntry(entry);
			
			QueriesManager.putSessionEntry(entry, user, session);

			LuceneAndMahoutUtilities lang = LuceneAndMahoutUtilities.getInstance();
			
			List<String> keywords = lang.tokenizerAndStemmer(entry);
			
			for (String keyword : keywords)
			{
				QueriesManager.putRecommandation(session, keyword);
			}
			
			JSONObject json = new JSONObject();

			json.put("response", "OK");

			return ServiceUtilities.formattedSuccessResponse(json.toString());
		} catch (Exception e)
		{
			logger.info(e.getMessage());
			return ServiceUtilities.formattedFailResponse();
		}
	}
}
