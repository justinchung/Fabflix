
function handleAtcClick() {
	console.log("click detected");
	var movieId = document.getElementById("movieId").innerHTML;
	
	$.ajax({
		data: {
			"movieId": movieId
		},
		method: "POST",
		url: "/project2/ShowSession",
		success: function() {
			console.log("POST success");
			alert("Added to cart");
		},
		error: function(jqXHR, textStatus, errorThrown) { 
			console.log(jqXHR.status);
			console.log(textStatus);
			console.log(errorThrown); 
		},
	})
}

console.log("here");

$("#add_to_cart_button").unbind('click').click(handleAtcClick);