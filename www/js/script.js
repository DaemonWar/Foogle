$(function()
{
	$("#tere_close_button").click(closeTextReportsPopup);

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

	dwhType1Pattern = $("#dwh_type_1_pattern").html();
	$("#dwh_type_1_pattern").remove();

	dwhType2Pattern = $("#dwh_type_2_pattern").html();
	$("#dwh_type_2_pattern").remove();
});

var searchPattern;
var dwhType1Pattern;
var dwhType2Pattern;

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

		searchInTextReports();
	}
	
	if($("#dwh").hasClass("on"))
	{
		$("#dwh_tab").show();

		showDataWarehouseSearch();

		searchInDWH();
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
		//getResponseFromServer("reco", $.cookie("sessionId"), function(data)
		getResponseFromServer("reco", input, function(data)
		{
			$("#recommendations>span").remove();
			
			if (data.length != 0)
			{
				$("#recommendations_container").show();
			} else
			{
				$("#recommendations_container").hide();
			}
			var clickReco = function()
			{
				$(this).click(function(e)
				{
					$("#search_field").val($(this).text());
					$("#search_form").submit();
				});
			};

			var wordArray = [];

			data.forEach(function(entry)
			{
				entry = eval("(" + entry + ")");

				wordArray.push({text: entry.key, weight: entry.value, afterWordRender:clickReco})
			});
			
			$("#recommendations").jQCloud(wordArray);
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
		getResponseFromServer("query", "save/" + input + "/" + $.cookie("userId") + "/" + $.cookie("sessionId"));
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

		if(data.results.length == 0)
		{
			$("#dbp_result").append("No result");
		}
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

function searchInDWH()
{
	$("#dwh_result").html("");

	getResponseFromServer("find", "dwh/" + $("#search_field").val(), function(data)
	{
		if(data.type != null)
		{
			if(data.type == 1)
			{
				data.result.forEach(function(item)
				{
					var obj = $(dwhType1Pattern);
					obj.find(".stage").text("Match: " + item.stage);
					obj.find(".date").text(item.date);
					obj.find(".country:eq(0)").text(item.country1);
					obj.find(".country:eq(1)").text(item.country2);
					obj.find(".score").text(item.score1 + "-" + item.score2);

					$("#dwh_result").append(obj);
				});
			} else if (data.type == 2)
			{
				item = data.result;

				var obj = $(dwhType2Pattern);
				obj.find(".header").text(item.header);
				obj.find(".years").text(item.title + ":");
				item.data.forEach(function(year)
				{
					obj.append($("<span class=\"host\"></span>").text(year));
				});

				if(item.data.length == 0)
				{
					obj.append($("<span class=\"host\"></span>").text("Never hosting World Cup"));
				}

				$("#dwh_result").append(obj);
			} else
			{
				$("#dwh_result").html(data.type);
			}
		} else
		{
			$("#dwh_result").html("No result");
		}
	});
}

function searchInTextReports()
{
	$("#tere_result").html("");

	getResponseFromServer("find", "mongo/" + $("#search_field").val(), function(data)
	{
		data.forEach(function(entry)
		{
			var obj = $(searchPattern);
			obj.find("a").text(entry.title).removeAttr("target").removeAttr("href");
			obj.find("span").text(entry.source);
			if(entry.content != undefined && entry.content != "")
			{
				obj.find("p").html(entry.content.replace(/\[[0-9].\]/g, ""));	
			} else
			{
				obj.find("p").text("No desciption");
			}

			obj.click(textReportsPopup);

			$("#tere_result").append(obj);
		});

		if(data.length == 0)
		{
			$("#tere_result").append("No result");
		}
	});
}

function textReportsPopup(e)
{
	var result = $(this);

	$(".tere_title").text(result.find("a").text());

	$(".tere_content").html(result.find("p").html());
	
	$(".tere_source").text(result.find("span").text());

	openTextReportsPopup();
}

function openTextReportsPopup(e)
{
	$("#tere_popup").fadeIn(200);
}

function closeTextReportsPopup(e)
{
	$("#tere_popup").fadeOut(200);
}