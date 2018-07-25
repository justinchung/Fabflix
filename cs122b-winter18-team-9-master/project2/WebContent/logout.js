
function handleLogout() {
	$.ajax({
		data: {
			"logout": "true"
		},
		method: "POST",
		url: "/project2/Logout",
		success: function() {
			window.location.replace("/project2/login.html");
		},
		error: function(jqXHR, textStatus, errorThrown) { 
			console.log(jqXHR.status);
			console.log(textStatus);
			console.log(errorThrown); 
		}
	})
	
}

$("#logout_button").click(handleLogout);