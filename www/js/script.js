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

	searchPattern = $("#search_pattern").html();
	$("#search_pattern").remove();
});

var searchPattern;

var searchMode = false;

function updateTabButton()
{
	$(".search_tab").hide();

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

function searchStep()
{
	updateTabButton();

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
		getResponseFromServer("query", "put/" + input, function(data)
			{
				console.log(data);
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