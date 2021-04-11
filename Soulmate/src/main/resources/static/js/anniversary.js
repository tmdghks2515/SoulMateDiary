$("nav ul a").eq(1).addClass("active");

$("#showForm").click(() => $("form").toggleClass("hidden"));

$("#frmAnni a").click(() => {
	
	let anniName = $("input[name=anniName]").val();
	let date = $("input[name=date]").val();
	
	if(anniName == ""  || date == ""){
		alert("모든 정보를 입력하세요.");
		return;
	}
	
	$.ajax({
		method: 'POST',
		url: '/user/addAnniversary',
		data: {
			'anniName': anniName,
			'date': date
		},
		success: (resp) => {
			location.href='/user/anniversary';
		}
	})
});


/* The Modal */
// Get the modal
var modal = document.getElementById("myModal");

// Get the <span> element that closes the modal
var span = document.getElementsByClassName("close")[0];


const openModal = (anniName, anniDate, story, id) => {
	$("#name").html(anniName);
	$("#date").html(anniDate);
	$("#story").val(story);
	$("#id").val(id);
	modal.style.display = "block";
	
	const now = new Date();
	const date = Date.parse(anniDate);
	$(".modal-content textarea").attr("placeholder","어떤 일이 있었나요?");
	if(date > now){
		$(".modal-content textarea").attr("placeholder","어떤 일을 계획하고 계시나요?");
	}
}

$(".modal-content a").click(() => {
	$.ajax({
		method: 'POST',
		url: '/user/saveStory',
		data: {
			'id': $("#id").val(),
			'story': $("#story").val(),
		},dataType: 'json',
		success: (resp) => {
			alert(resp.responseMessage);
			location.href= '/user/anniversary'
		}
	})
})


// When the user clicks on <span> (x), close the modal
span.onclick = function() {
  modal.style.display = "none";
}

// When the user clicks anywhere outside of the modal, close it
window.onclick = function(event) {
  if (event.target == modal) {
    modal.style.display = "none";
  }
}


/* /The Modal  */



for(i=0 ; i<$(".count").length ; i++){
	let str = $(".count").eq(i).text();
	if(str.length == 3 && str.substring(1,3) < 0 && str.substring(1,3) > -8){
		$(".count").eq(i).parent().parent().addClass("yellow");
	}else if(str == "D-day !!!"){
		$(".count").eq(i).parent().parent().addClass("red");
	}
}
