package com.foogle.rest;

import java.util.ArrayList;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import org.json.JSONArray;

import com.foogle.model.manager.QueriesManager;
import com.foogle.rest.utils.ServiceUtilities;

@Path("/reco")
public class RecommendationsService
{
	@GET
	@Path("/for/{query}")
	public Response getReco(@PathParam("query") String entry)
	{
		JSONArray json = new JSONArray();

		ArrayList<String> queries = QueriesManager.findRecommendationsFor(entry);
		
		for(String query : queries)
		{
			json.put(query);
		}
		
		return ServiceUtilities.formattedSuccessResponse(json.toString());
	}
}