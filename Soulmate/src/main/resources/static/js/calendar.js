
$("nav ul a").eq(2).addClass("active");

(function() {
	calendarMaker($("#calendarForm"), new Date());
})();

var nowDate = new Date();
function calendarMaker(target, date) {
	if (date == null || date == undefined) {
		date = new Date();
	}
	nowDate = date;
	if ($(target).length > 0) {
		var year = nowDate.getFullYear();
		var month = nowDate.getMonth() + 1;
		$(target).empty().append(assembly(year, month));
	} else {
		console.error("custom_calendar Target is empty!!!");
		return;
	}

	var thisMonth = new Date(nowDate.getFullYear(), nowDate.getMonth(), 1);
	var thisLastDay = new Date(nowDate.getFullYear(), nowDate.getMonth() + 1, 0);

	var tag = "<tr>";
	let cnt = 0;
	//빈 공백 만들어주기
	for (i = 0; i < thisMonth.getDay(); i++) {
		tag += "<td></td>";
		cnt++;
	}
	//날짜 채우기
	for (i = 1; i <= thisLastDay.getDate(); i++) {
		if (cnt % 7 == 0) { tag += "<tr>"; }

		tag += "<td>" + i + "</td>";
		cnt++;
		if (cnt % 7 == 0) {
			tag += "</tr>";
		}
	}
	$(target).find("#custom_set_date").append(tag);
	calMoveEvtFn();

	function assembly(year, month) {
		var calendar_html_code =
			"<table class='custom_calendar_table'>" +
			"<colgroup>" +
			"<col style='width:81px'/>" +
			"<col style='width:81px'/>" +
			"<col style='width:81px'/>" +
			"<col style='width:81px'/>" +
			"<col style='width:81px'/>" +
			"<col style='width:81px'/>" +
			"<col style='width:81px'/>" +
			"</colgroup>" +
			"<thead class='cal_date'>" +
			"<th><button type='button' class='prev icon-left-open'></button></th>" +
			"<th colspan='5'><p><span>" + year + "</span>년 <span>" + month + "</span>월</p></th>" +
			"<th><button type='button' class='next icon-right-open'></button></th>" +
			"</thead>" +
			"<thead  class='cal_week'>" +
			"<th>일</th><th>월</th><th>화</th><th>수</th><th>목</th><th>금</th><th>토</th>" +
			"</thead>" +
			"<tbody id='custom_set_date'>" +
			"</tbody>" +
			"</table>";
		return calendar_html_code;
	}

	function calMoveEvtFn() {
		//전달 클릭
		$(".custom_calendar_table").on("click", ".prev", function() {
			nowDate = new Date(nowDate.getFullYear(), nowDate.getMonth() - 1, nowDate.getDate());
			calendarMaker($(target), nowDate);
			$("td").click(function() {
				modal.style.display = "block";
				let date = String(nowDate.getFullYear()) + "-" + (nowDate.getMonth() + 1) + "-" + $(this).text().substring(0, 2);
				$(".modal-content p").text(date);
				$(".modal-content input[type=hidden]").val(date);
			})
		});
		//다음달 클릭
		$(".custom_calendar_table").on("click", ".next", function() {
			nowDate = new Date(nowDate.getFullYear(), nowDate.getMonth() + 1, nowDate.getDate());
			calendarMaker($(target), nowDate);
			$("td").click(function() {
				modal.style.display = "block";
				let date = String(nowDate.getFullYear()) + "-" + (nowDate.getMonth() + 1) + "-" + $(this).text().substring(0, 2);
				$(".modal-content p").text(date);
				$(".modal-content input[type=hidden]").val(date);
			})
		});
	}

	// 이번달 기념일 list, 스케듈 list 서버에서 받아오기
	let anniList;
	let scheduleList;
	$.ajax({
		url: '/user/getMonthAnni',
		data: { 'anniMonth': nowDate.getMonth() + 1, 'anniYear': nowDate.getFullYear() },
		dataType: 'json',
		async: false,
		success: (resp) => {
			anniList = resp.result;
			scheduleList = resp.scheduleList;
		}
	})
	// 기념일 달력에 뿌리기
	for (i = 0; i < anniList.length; i++) {
		let no = anniList[i].anniDate;
		no = no.substring(8, 10);
		$("#custom_set_date td").eq(Number(thisMonth.getDay()) + Number(no) - 1)
			.append("<br><span class='anni'>" + anniList[i].anniName + "</span>")
	}
	// 스케듈 달력에 뿌리기
	for (i = 0; i < scheduleList.length; i++) {
		let no = scheduleList[i].scheduleTime;
		no = no.substring(8, 10);
		$("#custom_set_date td").eq(Number(thisMonth.getDay()) + Number(no) - 1)
			.append('<p class="schedule">' + scheduleList[i].scheduleName + '</p>');
	}
	

	let today = new Date();
	if (date.getFullYear() == today.getFullYear() && date.getMonth() == today.getMonth()) {
		$("#custom_set_date td").eq(
			Number(thisMonth.getDay() + Number(today.getDate()) - 1)).addClass("select_day");
	}

	$(".anni").parent().css("border", "3px solid #E2C547");
}

// Get the modal
var modal = document.getElementById("myModal");

// Get the button that opens the modal
var btn = document.getElementById("myBtn");

// Get the <span> element that closes the modal
var span = document.getElementsByClassName("close")[0];

// When the user clicks the button, open the modal 
$("td").click(function() {
	modal.style.display = "block";
	let date = String(nowDate.getFullYear()) + "-" + (nowDate.getMonth() + 1) + "-" + $(this).text().substring(0, 2);
	$(".modal-content p").text(date);
	$(".modal-content input[type=hidden]").val(date);
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

