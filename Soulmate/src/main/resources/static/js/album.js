$("nav li a").eq(0).addClass("active");

$("#btnAddCard").click(() => $("form").eq(0).toggleClass("hidden"));
$("#btnShowSearch").click(() => $("form").eq(1).toggleClass("hidden"));

let count = 1;
const plus = () => {
	if (count >= 5) {
		alert("최대 5장 입니다.");
		return;
	}
	count++;
	$("form div").eq(0).append('	<p><input type="file" name="file" accept="image/x-png,image/gif,image/jpeg"></p>');
}
const minus = () => {
	if (count <= 1) {
		return;
	}
	count--;
	$("form p").last().remove();
}

$("#plus").click(() => {
	plus();
})

$("#minus").click(() => {
	minus();
})

$("form a").click(() => {
	if($("form input[type=file]")[0].files.length == 0){
		alert('사진을 선택해 주세요');
		return;
	}
	if($("form input[type=text]")[0].value == ""){
		alert('제목을 작성해 주세요');
		return;
	}
	if($("form textarea")[0].value == ""){
		alert('내용을 작성해 주세요');
		return;
	}
	$("form").submit();
});


/** */
// Get the modal
var modal = document.getElementById("myModal");

// Get the <span> element that closes the modal
var span = document.getElementsByClassName("close")[0];

// When the user clicks the button, open the modal 
const showModal = (title, soulmateId) => {
	$(".modal-header h2").text(title);
	modal.style.display = "block";
	let len = 0;
	let content = "";
	let date = "";
	$.ajax({
		url: '/user/showImgs',
		data: { title: title, soulmateId: soulmateId },
		async: false,
		dataType: 'json', 
		success: (resp) => {
			console.log(resp);
			len = resp.len;
			content = resp.content;
			date = resp.date;
		}
	})
	let str = "<div>";
	for (i = 0; i < len; i++) {
		str += '<img src="/user/showImg?soulmateId=' + soulmateId + '&title=' + title + '&no=' + i + '">';
	}
	str += '</div><div><span class="sub">한줄일기:</span> '+content+'<br><span class="sub">업로드 날짜: </span>'+date+'</div>';
	$(".modal-body").html(str);

}
// When the user clicks on <span> (x), close the modal
span.onclick = function() {
	modal.style.display = "none";
}

/** */

const doSearch = () => {
	$.ajax({
		url: '/user/doSearch',
		data: {title: $("#frmSearch input").val()},
		dataType: 'json',
		success: (resp) => {
			let str = "";
			const result = resp.result;
			for(i=0; i<result.length; i++){
				str += '<div class="card" onclick="showModal('+`'`+result[i].title+`'`+','+`'`+result[i].soulmate.id+`'`+')">'
							+'<div><img src="/user/showImg?soulmateId='+result[i].soulmate.id+'&title='+result[i].title+'"></div>'
							+'<div class="noselect">'+result[i].title+'<br>'+result[i].createDate.substr(0,10)+'</div>'
							+'</div>';
			}
			$("#cards").html(str);
		}
	})
}