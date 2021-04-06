$("a").click((e) => {
	e.preventDefault();
	let data = $("form").serialize();
	$.ajax({
		method: "POST",
		url:"/user/connectSoulmateProc",
		data: data,
		dataType: "json",
		success: (resp) => {
			console.log(resp);
			switch(resp.responseCode){
				case 1:
					alert(resp.responseMessage);
					window.close();
					break;
				case 2:
					alert(resp.responseMessage);
					break;
			}			
		} 
	});
})