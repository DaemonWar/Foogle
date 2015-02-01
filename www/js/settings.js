$(function()
{
	$("#settings_button").click(openSettings);

	$("#settings_close_button").click(closeSettings);

	$(".option").click(function(e)
	{
		var count = $(".option.on").size();

		var toOff = $(this).hasClass("on");

		if(count == 1)
		{
			if(!toOff)
			{
				$(this).toggleClass("on");
			}
		} else
		{
			$(this).toggleClass("on");
		}

		saveOptions();
	});

	initOptions();
});

function initOptions()
{
	if($.cookie('allowCookie') == 'true')
	{
		if($.cookie('dwh') == undefined)
		{
			$.cookie('dwh', true);
		} else if($.cookie('dwh') == "false")
		{
			$("#dwh").removeClass("on");
		}

		if($.cookie('tere') == undefined)
		{
			$.cookie('tere', true);
		} else if($.cookie('tere') == "false")
		{
			$("#tere").removeClass("on");
		}

		if($.cookie('dbp') == undefined)
		{
			$.cookie('dbp', true);
		} else if($.cookie('dbp') == "false")
		{
			$("#dbp").removeClass("on");
		}

		if($.cookie('web') == undefined)
		{
			$.cookie('web', true);
		} else if($.cookie('web') == "false")
		{
			$("#web").removeClass("on");
		}
	}
}

function saveOptions()
{
	if($.cookie('allowCookie') == 'true')
	{
		$.cookie('dwh', $("#dwh").hasClass("on"));

		$.cookie('tere', $("#tere").hasClass("on"));
		
		$.cookie('dbp', $("#dbp").hasClass("on"));

		$.cookie('web', $("#web").hasClass("on"));
	}
}

function openSettings(e)
{
	$("#settings").fadeIn(200);
}

function closeSettings(e)
{
	$("#settings").fadeOut(200);
}