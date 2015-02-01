//var serverAddress = "http://89.84.210.37:8080/Foogle/rest/";
//var serverAddress = "http://192.168.1.10:8080/Foogle/rest/";
var serverAddress = "http://localhost:8080/Foogle/rest/";

function getResponseFromServer(service, fct, callback)
{
	//console.log(service + "/" + fct);

	$.ajax(
	{
		type: "GET",
		url: serverAddress + service + "/" + fct,
		dataType: 'json',
		crossDomain: true
	}).done(function(data)
	{
		if(callback)
		{
			callback(data);
		}
	}).fail(function()
	{
		console.log("Server problem");
	});
}

function getResponseDbPedia(input, callback)
{
	$.ajax(
	{
		type: "GET",
		url: "http://lookup.dbpedia.org/api/search/KeywordSearch?MaxHits=50&QueryString=" + input,
		dataType: 'json',
		crossDomain: true
	}).done(function(data)
	{
		if(callback)
		{
			callback(data);
		}
	}).fail(function()
	{
		console.log("Server problem");
	});
}

function getResponseWeb(input, callback, webLoop, fullWebData)
{
	if(!webLoop)
	{
		webLoop = 0;
	}

	if(!fullWebData)
	{
		fullWebData = [];
	}

	$.ajax(
	{
		type: "GET",
		url: "http://ajax.googleapis.com/ajax/services/search/web?v=1.0&rsz=8&q=" + input + "&start=" + webLoop,
		dataType: 'jsonp',
		crossDomain: true
	}).done(function(data)
	{
		if(webLoop != 80 && data.responseData != null) 
		{
			webLoop += 8;

			fullWebData = fullWebData.concat(data.responseData.results);

			getResponseWeb(input, callback, webLoop, fullWebData);
		} else
		{
			webLoop = 0;

			if(callback)
			{
				callback(fullWebData);
			}

			fullWebData = [];
		}
	}).fail(function()
	{
		console.log("Server problem");
	});
}