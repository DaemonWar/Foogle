package com.foogle.rest;

import java.util.ArrayList;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import com.foogle.model.manager.QueriesManager;
import com.foogle.rest.utils.ServiceUtilities;

@Path("/query")
public class QueryService
{
	@GET
	@Path("/autocomplete/{query}")
	public Response findQueriesFor(@PathParam("query") String entry)
	{
		JSONArray json = new JSONArray();

		ArrayList<String> queries = QueriesManager.findQueriesFor(entry);
		
		for(String query : queries)
		{
			json.put(query);
		}
		
		return ServiceUtilities.formattedSuccessResponse(json.toString());
	}
	
	@GET
	@Path("/put/{query}")
	public Response put(@PathParam("query") String entry)
	{
		try
		{
			JSONObject json = new JSONObject();
			
			json.put("response", "OK");
			
			QueriesManager.putEntry(entry);
			
			return ServiceUtilities.formattedSuccessResponse(json.toString());
		} catch (Exception e) 
		{
			return ServiceUtilities.formattedFailResponse();
		}
	}
	
	@GET
	@Path("/save/{query}/{user}/{session}")
	public Response save(@PathParam("query") String entry, @PathParam("user") String user, @PathParam("session") String session)
	{
		try
		{
			JSONObject json = new JSONObject();
			
			json.put("response", "OK");
			
			QueriesManager.putSessionEntry(entry, user, session);
			
			return ServiceUtilities.formattedSuccessResponse(json.toString());
		} catch (Exception e) 
		{
			return ServiceUtilities.formattedFailResponse();
		}
	}
}
