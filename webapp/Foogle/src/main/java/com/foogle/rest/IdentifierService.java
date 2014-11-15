package com.foogle.rest;

import java.util.Date;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.json.JSONObject;

import com.foogle.rest.utils.ServiceUtilities;

@Path("/id")
public class IdentifierService
{
	@GET
	@Path("/newUser")
	public Response getNewUser()
	{
		return getId("u_");
	}
	
	@GET
	@Path("/newSession")
	public Response getNewSession()
	{
		return getId("s_");
	}
	
	private Response getId(String prefix)
	{
		try
		{
			JSONObject json = new JSONObject();
			json.put("id", prefix + getRandomId(getTimeStamp()));
			 
			return ServiceUtilities.formattedSuccessResponse(json.toString());
		} catch (Exception e) 
		{
			return ServiceUtilities.formattedFailResponse();
		}
	}
	
	private String getTimeStamp()
	{
		Date d = new Date();
		
		return String.valueOf(d.getTime());
	}
	
	private String getRandomId(String base)
	{
		String rid = base + String.valueOf(Math.round(Math.random() * 1000));
		
		return rid;
	}
}