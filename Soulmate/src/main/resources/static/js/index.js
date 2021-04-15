// MainCarousel entity 서버에서 불러오기
$.ajax({
	url: 'getCarousels',
	dataType: 'json',
	success: (resp) => {
		const carouselList = resp.result;
		for (i = 0; i < carouselList.length; i++) {
			let str = String(i + 1);
			$("#title" + str).text(carouselList[i].title);
			$("#content" + str).text(carouselList[i].content);
		}
	}
})

const updateCarousel = (page) => {
	
	let title = $(".carouselTitle").eq(page-1).val();
	let content = $(".carouselContent").eq(page-1).val();
	$.ajax({
		method: 'POST',
		url: '/admin/updateCarousel',
		data: { 'title': title, 'content': content, 'page': page },
		dataType: 'json',
		success: (resp) => {
			if (resp.responseCode === 200) {
				alert(resp.responseMessage);
				location.href = '/';
			};
		}
	})

}

// 캐러셀(슬라이더) 적용
$('.slider').slick({
	dots: true,
	fade: true,
	cssEase: 'linear',
	autoplay: false,
	prevArrow: $('.prev'),
	nextArrow: $('.next')
});

// 사진 변경
function changeImage(page) {
	$(".picture form").eq(page-1).submit();
}
