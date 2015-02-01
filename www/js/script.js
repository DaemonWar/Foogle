$(function()
{
	$(document).keypress(function()
	{
		$("#search_field").focus();
	});

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

	$("#logo_small").click(function(e)
	{
		window.location.href = "./";
	});

	$("#search_field").focus();

	$("#dwh_tab").click(showDataWarehouseSearch);

	$("#tere_tab").click(showTextReportsSearch);

	$("#dbp_tab").click(showDbPediaSearch);

	$("#web_tab").click(showWebSearch);

	searchPattern = $("#search_pattern").html();
	$("#search_pattern").remove();
});

var searchPattern;

var searchMode = false;

function updateTabButton()
{
	$(".search_tab").hide();
	
	if($("#web").hasClass("on"))
	{
		$("#web_tab").show();

		showWebSearch();

		searchOnWeb();
	}

	if($("#dbp").hasClass("on"))
	{
		$("#dbp_tab").show();

		showDbPediaSearch();

		searchOnDbPedia();
	}

	if($("#tere").hasClass("on"))
	{
		$("#tere_tab").show();

		showTextReportsSearch();
	}
	
	if($("#dwh").hasClass("on"))
	{
		$("#dwh_tab").show();

		showDataWarehouseSearch();
	}
}

function showDataWarehouseSearch(e)
{
	$(".results_list").hide();

	$(".search_tab").removeClass("on");

	$("#dwh_tab").addClass("on");

	$("#dwh_result").show();
}

function showTextReportsSearch(e)
{
	$(".results_list").hide();

	$(".search_tab").removeClass("on");

	$("#tere_tab").addClass("on");

	$("#tere_result").show();
}

function showDbPediaSearch(e)
{
	$(".results_list").hide();

	$(".search_tab").removeClass("on");

	$("#dbp_tab").addClass("on");

	$("#dbp_result").show();
}

function showWebSearch(e)
{
	$(".results_list").hide();

	$(".search_tab").removeClass("on");

	$("#web_tab").addClass("on");

	$("#web_result").show();
}

function updateRecommendations()
{
	var input = $("#search_field").val();

	if (input != "")
	{
		getResponseFromServer("reco", "for/" + input, function(data)
		{
			$("#recommendations>span").remove();

			$("#recommendations_container").hide();

			if(data.length != 0)
			{
				data.forEach(function(entry)
				{
					$("#recommendations").append($("<span></span>").text(entry));
				});

				$("#recommendations>span").click(function(e)
				{
					$("#search_field").val($(this).text());
					$("#search_form").submit();
				});
				
				$("#recommendations_container").show();
			}
		});
	}
}

function searchStep()
{
	updateTabButton();

	updateRecommendations();

	saveQuery();

	if(!searchMode)
	{
		$(this).off("input");

		$("#logo_small").css("display", "inline-block");

		$("#results").css("display", "block");

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

		searchMode = true;
	}
}

function saveQuery()
{
	var input = $("#search_field").val();

	if (input != "")
	{
		getResponseFromServer("query", "put/" + input, function()
		{
			if($.cookie("userId") != undefined && $.cookie("sessionId") != undefined)
			{
				getResponseFromServer("query", "save/" + input + "/" + $.cookie("userId") + "/" + $.cookie("sessionId"));
			}
		});
	}
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

function searchOnDbPedia()
{
	$("#dbp_result").html("");

	getResponseDbPedia($("#search_field").val(), function(data)
	{
		data.results.forEach(function(entry)
		{
			var obj = $(searchPattern);
			obj.find("a").attr("href", entry.uri).text(entry.label);
			obj.find("span").text(entry.uri);
			if(entry.description != undefined && entry.description != "")
			{
				obj.find("p").text(entry.description);	
			} else
			{
				obj.find("p").text("No desciption");
			}

			$("#dbp_result").append(obj);
		});
	});
}

function searchOnWeb()
{
	$("#web_result").html("");

	getResponseWeb($("#search_field").val(), function(data)
	{
		data.forEach(function(entry)
		{
			var obj = $(searchPattern);
			obj.find("a").attr("href", entry.url).text(entry.titleNoFormatting);
			obj.find("span").text(entry.url);
			if(entry.content != undefined && entry.content != "")
			{
				obj.find("p").html(entry.content);	
			} else
			{
				obj.find("p").text("No desciption");
			}

			$("#web_result").append(obj);
		});
	});
}