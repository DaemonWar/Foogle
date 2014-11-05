//var serverAddress = "http://89.84.210.37:8080/Foogle/rest/";
//var serverAddress = "http://192.168.1.10:8080/Foogle/rest/";
var serverAddress = "http://localhost:8080/Foogle/rest/";

function getResponseFromServer(service, fct, callback)
{
	$.ajax(
	{
		type: "GET",
		url: serverAddress + service + "/" + fct,
		dataType: 'json',
		crossDomain: true
	}).done(function(data)
	{
		callback(data);
	})
	.fail(function(a, b, c)
	{
		//TODO
	});
}