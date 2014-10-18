var waitForAutocomplete = 500;

var currentAutocomplete = null;
var currentSelectedAutocomplete = null;

function showAutocomplete()
{
	$("#autocomplete").css("display", "block");
}

function hideAutocomplete()
{
	$("#autocomplete").css("display", "none");

	$("#autocomplete>ul>li").removeClass("active");

	currentSelectedAutocomplete = null;
}


$(function()
{
	$(document).keypress(function()
	{
		$("#search_field").focus();
	});

	$("#search_field").on("input", function(e)
	{
		if(currentAutocomplete)
		{
			clearTimeout(currentAutocomplete);
		}

		if($(this).val() != "")
		{
			currentAutocomplete = setTimeout(function()
			{
				showAutocomplete();

				currentAutocomplete = null;
			}, waitForAutocomplete);
		} else
		{
			hideAutocomplete();
		}
	});

	$("#search_field").keydown(function(e)
	{
		switch(e.which)
		{
			case 40: //bas
				e.preventDefault();
				
				var count = $("#autocomplete>ul>li").size();

				if(currentSelectedAutocomplete == null)
				{
					currentSelectedAutocomplete = 1;
				} else
				{
					currentSelectedAutocomplete = currentSelectedAutocomplete + 1 > count ? 1 : currentSelectedAutocomplete + 1;
				}
				$("#autocomplete>ul>li").removeClass("active");

				var obj = $("#autocomplete>ul>li:nth-child(" + currentSelectedAutocomplete + ")");

				obj.addClass("active");

				$("#search_field").val(obj.text());

				break;
			case 38: //haut
				e.preventDefault();
				
				var count = $("#autocomplete>ul>li").size();

				if(currentSelectedAutocomplete == null)
				{
					currentSelectedAutocomplete = count;
				} else
				{
					currentSelectedAutocomplete = currentSelectedAutocomplete - 1 < 1 ? count : currentSelectedAutocomplete - 1;
				}

				$("#autocomplete>ul>li").removeClass("active");

				var obj = $("#autocomplete>ul>li:nth-child(" + currentSelectedAutocomplete + ")");

				obj.addClass("active");

				$("#search_field").val(obj.text());

				break;
    	}
	});

	$("#autocomplete>ul>li").click(function(e)
	{
		$("#search_field").val($(this).text());

		$("#search_form").submit();
	});

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
		});
	} else if($.cookie('allowCookie') == 'true')
	{
		$("#allow_cookie").remove();
	}

	$("#search_field").focus(function(e)
	{
		$(this).parent().addClass("active");
	});

	$("#search_field").blur(function(e)
	{
		$(this).parent().removeClass("active");
	});

	$("#logo>.two").click(animBall);

	$("#search_form").submit(function(e)
	{
		e.preventDefault();

		searchStep();

		$("#search_field").blur();

		if(currentAutocomplete)
		{
			clearTimeout(currentAutocomplete);
		}

		hideAutocomplete();
	});

	$("#settings_button").click(openSettings);

	$("#settings_close_button").click(closeSettings);

	$("#logo_small").click(function(e)
	{
		window.location.href = "./";
	});

	$("#search_field").focus();
});

var settingsModified = false;

function openSettings(e)
{
	$("#settings").fadeIn(200);
}

function closeSettings(e)
{
	if(settingsModified)
	{
		//TODO demander d'enregistrer
	}

	$("#settings").fadeOut(200);
}

function searchStep()
{
	$(this).off("input");

	$("#logo_small").css("display", "inline-block");

	$("header").css(
	{
		"background-color" : "#EBEFF3",
		"height" : "60px"
	});

	$("#search_form").css(
	{
		"display": "inline-block",
		"margin" : "15px 0px"
	});

	$("#search_button").css(
	{
		"background-color" : "#3A9D23",
		"background-image" : "url('./img/loupe2.png')",
		"right" : "0"
	});

	$("#logo").remove();
}

function animBall(e)
{
	$(this).off("click");

	$(this).animate(
	{
		bottom : 110
	},
	800,
	"easeOutQuad",
	function()
	{
		$(this).animate(
		{
			bottom : 34
		},
		1500,
		"easeOutBounce",
		function()
		{
			$(this).click(animBall);
		});
	});
}