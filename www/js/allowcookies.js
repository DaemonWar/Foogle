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

			getIdentifier();
		});
	} else if($.cookie('allowCookie') == 'true')
	{
		$("#allow_cookie").remove();

		getIdentifier();
	}
});

function getIdentifier()
{
	if($.cookie('userID') == undefined)
	{
		getResponseFromServer("identifier", "get.json", function(data)
		{
			$.cookie('userID', data.id);
		});
	}
}