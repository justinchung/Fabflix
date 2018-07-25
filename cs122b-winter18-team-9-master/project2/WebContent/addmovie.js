
function addMovieDashBoard() {
	var form = document.forms["add_movie_form"];
	
	$.ajax({
		data: {
			"title": form.elements[0].value,
			"year": form.elements[1].value,
			"director": form.elements[2].value,
			"genre": form.elements[3].value,
			"star": form.elements[4].value
		},
		method: "POST",
		url: "/project2/AddMovie",
		success: function() {
			console.log("add movie success");
			alert("add movie success!");
		},
		error: function(jqXHR, textStatus, errorThrown) { 
			console.log(jqXHR.status);
			console.log(textStatus);
			console.log(errorThrown); 
			alert("fail");
		}
	})
}

$("#add_movie").click(addMovieDashBoard);