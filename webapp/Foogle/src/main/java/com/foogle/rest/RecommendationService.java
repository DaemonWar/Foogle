package com.foogle.rest;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.codehaus.jettison.json.JSONObject;
import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.foogle.model.dao.DAOWordEntries;
import com.foogle.model.manager.QueriesManager;
import com.foogle.rest.utils.ServiceUtilities;

@Path("/reco")
public class RecommendationService
{
	private final Logger logger = LoggerFactory.getLogger(getClass());

	@GET
	@Path("/{query}")
	public Response getReco(@PathParam("query") String sessionId)
	{
		try
		{
			JSONArray json = new JSONArray();

			List<RecommendedItem> keywords = QueriesManager.findRecommendationsFor(sessionId);
			
			DAOWordEntries dwe = new DAOWordEntries();

			for (RecommendedItem keyword : keywords)
			{
				logger.info("Item : " + dwe.find((int) keyword.getItemID()).getWord());
				logger.info("Value : " + String.valueOf(keyword.getValue()));
				
				JSONObject jo = new JSONObject();

				jo.put(dwe.find((int) keyword.getItemID()).getWord(), String.valueOf(keyword.getValue()));
				
				json.put(jo);
			}

			return ServiceUtilities.formattedSuccessResponse(json.toString());
		} catch (Exception e)
		{
			logger.info(e.getMessage());
			
			return null;
		}
	}
}