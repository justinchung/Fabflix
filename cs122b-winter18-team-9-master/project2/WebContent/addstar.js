
function addStarDashboard() {
	
	var form = document.forms["insert_star_form"];
	
	$.ajax({
		data: {
			"name": form.elements[0].value,
			"birthyear": form.elements[1].value
		},
		method: "POST",
		url: "/project2/AddStar",
		success: function() {
			console.log("add star success");
		},
		error: function(jqXHR, textStatus, errorThrown) { 
			console.log(jqXHR.status);
			console.log(textStatus);
			console.log(errorThrown); 
			alert("fail");
		}
	})
}


$("#add_star").click(addStarDashboard);