$("nav li a").eq(3).addClass("active");

$(".title").click(function(){
	let index = $(".title").index($(this));
	$(".content").eq(index).slideToggle(300);
})

const writeComment = () => $("#writeComment").submit();	
