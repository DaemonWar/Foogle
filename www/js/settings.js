$(function()
{
	$("#settings_button").click(openSettings);

	$("#settings_close_button").click(closeSettings);

	$("#connect").click(processConnect);

	$(".option, .option_bis").click(function(e)
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

		if($.cookie('aggre') == undefined)
		{
			$.cookie('aggre', true);
		} else if($.cookie('aggre') == "false")
		{
			$("#aggre").removeClass("on");
		}

		if($.cookie('account') == undefined)
		{
			$.cookie('account', false);
		} else if($.cookie('account') != "false")
		{
			connect($.cookie('account'));
		}
	}
}

var connected = false;

function processConnect(e)
{
	if(connected)
	{
		disconnect();
	} else
	{
		connect($("#account").val());
	}
}

function connect(name)
{
	connected = true;

	$.cookie('account', name);

	$("#account").val(name);
	$("#account").prop('disabled', true);

	$("#connect").addClass("off");
	$("#connect").text("Disconnect");
}

function disconnect()
{
	connected = false;

	$.cookie('account', false);

	$("#account").val("");
	$("#account").prop('disabled', false);

	$("#connect").removeClass("off");
	$("#connect").text("Connect");
}

function saveOptions()
{
	if($.cookie('allowCookie') == 'true')
	{
		$.cookie('dwh', $("#dwh").hasClass("on"));

		$.cookie('tere', $("#tere").hasClass("on"));
		
		$.cookie('dbp', $("#dbp").hasClass("on"));

		$.cookie('web', $("#web").hasClass("on"));

		$.cookie('aggre', $("#aggre").hasClass("on"));
	}
}

function openSettings(e)
{
	$("#settings").fadeIn(200);

	$(document).off("keypress");
}

function closeSettings(e)
{
	$("#settings").fadeOut(200);

	$(document).keypress(function()
	{
		$("#search_field").focus();
	});
}