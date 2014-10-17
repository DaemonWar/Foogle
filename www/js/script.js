$(function()
{
	$("#search_field").focus(function()
	{
		$(this).parent().addClass("active");
	});

	$("#search_field").blur(function()
	{
		$(this).parent().removeClass("active");
	});

	$("#logo>.two").click(animBall);

	$("#search_field").on("input", searchStep);

	$("#search_field").focus();
});

function searchStep(e)
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
		"right" : "0",
		"width" : "38px"
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
	1500,
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