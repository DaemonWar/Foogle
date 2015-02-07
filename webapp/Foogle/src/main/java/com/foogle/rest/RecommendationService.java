package com.foogle.rest;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONObject;
import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.foogle.model.entities.SearchEntries;
import com.foogle.model.manager.QueriesManager;
import com.foogle.rest.utils.ServiceUtilities;

@Path("/reco")
public class RecommendationService
{
	private final Logger logger = LoggerFactory.getLogger(getClass());

	@GET
	@Path("/{session}")
	public Response getReco(@PathParam("session") String entry)
	{
		try
		{
			JSONArray json = new JSONArray();

			List<SearchEntries> queries = QueriesManager.findRecommendationsFor(entry);
			
			//DAOWordEntries dwe = new DAOWordEntries();
			logger.info(String.valueOf(queries.size()));

			for (SearchEntries query : queries)
			{
				if(!query.getQuery().equals(entry))
				{
					JSONObject jo = new JSONObject();
	
					jo.put("key", query.getQuery());
					jo.put("value", String.valueOf(query.getCount()));
					
					json.put(jo);
				}
			}

			return ServiceUtilities.formattedSuccessResponse(json.toString());
		} catch (Exception e)
		{
			logger.info(e.getMessage());
			
			return ServiceUtilities.formattedSuccessResponse(new JSONArray().toString());
		}
	}
}