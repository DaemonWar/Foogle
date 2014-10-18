package com.foogle.rest;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("/identifier")
public class IdentifierService
{
	@GET
	@Path("/get")
	public Response getUiid()
	{
		try
		{
			return Response.status(200).entity(getRandomId(getDateId())).build();
		} catch (Exception e) 
		{
			return Response.status(501).build();
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