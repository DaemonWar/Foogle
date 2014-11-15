$(function()
{
	if($.cookie('allowCookie') == undefined)
	{
		$.cookie('allowCookie', 'false');
	}

	if($.cookie('allowCookie') == 'false')
	{
		$("#allow_cookie").submit(function(e)
		{
			e.preventDefault();

			$.cookie('allowCookie', 'true');

			$("#allow_cookie").remove();

			getUserId();

			getSessionId();
		});
	} else if($.cookie('allowCookie') == 'true')
	{
		$("#allow_cookie").remove();

		getUserId();

		getSessionId();
	}
});

function getUserId()
{
	if($.cookie('userId') == undefined)
	{
		getResponseFromServer("id", "newUser", function(data)
		{
			$.cookie('userId', data.id);
		});
	}
}

function getSessionId()
{
	getResponseFromServer("id", "newSession", function(data)
	{
		$.cookie('sessionId', data.id);
	});
}