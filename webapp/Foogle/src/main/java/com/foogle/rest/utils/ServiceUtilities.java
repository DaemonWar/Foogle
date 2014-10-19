package com.foogle.rest.utils;

import javax.ws.rs.core.Response;

public class ServiceUtilities
{
	public static Response formattedSuccessResponse(String data)
	{
		return Response.status(200).entity(data).header("Access-Control-Allow-Origin", "*").header("Content-Type", "application/json").build();
	}

	public static Response formattedFailResponse()
	{
		return Response.status(501).build();
	}
}