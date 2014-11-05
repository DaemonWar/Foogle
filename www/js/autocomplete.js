var waitForAutocomplete = 200;

var currentAutocomplete = null;

var currentSelectedAutocomplete = null;

var autocompleteIsShown = false;

$(function()
{
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
			}, waitForAutocomplete);
		} else
		{
			hideAutocomplete();
		}
	});

	$("#search_field").keydown(function(e)
	{
		if(autocompleteIsShown)
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
	    }
	});
});

function autocompleteLiClick(obj)
{
	console.log("click !");
	$("#search_field").val($(obj).text());

	$("#search_form").submit();
}

function showAutocomplete()
{
	//getResponseFromServer("query", "autocomplete/" + $("#search_field").val(), function(data)
	//{
		data = ["lalala", "lilili", "lolololo"];
		$("#autocomplete>ul>*").remove();

		if(data.length == 0)
		{
			$("#autocomplete>ul").append("<span>No suggestion to display</span>");
		} else
		{
			data.forEach(function(entry)
			{
				$("#autocomplete>ul").append("<li onclick='autocompleteLiClick(this)'>" + entry + "</li>");
			});

		autocompleteIsShown = true;
		}

		$("#autocomplete").css("display", "block");

		currentAutocomplete = null;
	//});
}

function hideAutocomplete()
{
	$("#autocomplete").css("display", "none");

	$("#autocomplete>ul>li").removeClass("active");

	currentSelectedAutocomplete = null;

	autocompleteIsShown = false;
}