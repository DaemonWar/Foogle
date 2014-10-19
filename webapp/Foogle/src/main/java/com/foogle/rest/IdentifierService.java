package com.foogle.rest;

import java.util.Date;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.json.JSONObject;

import com.foogle.rest.utils.ServiceUtilities;

@Path("/identifier")
public class IdentifierService
{
	@GET
	@Path("/get.json")
	public Response getUiid()
	{
		try
		{
			JSONObject json = new JSONObject();
			json.put("id", getRandomId(getDateId()));
			 
			return ServiceUtilities.formattedSuccessResponse(json.toString());
		} catch (Exception e) 
		{
			return ServiceUtilities.formattedFailResponse();
		}
	}
	
	private String getDateId()
	{
		Date d = new Date();
		
		return String.valueOf(d.getTime());
	}
	
	private String getRandomId(String base)
	{
		String rid = base + String.valueOf(Math.random() * 1000);
		
		return rid;
	}
}