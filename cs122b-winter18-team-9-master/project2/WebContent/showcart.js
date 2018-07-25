
function handlePayment() {
	window.location.replace("/project2/thankyou.html");
}

function showShoppingCart(resultData) {
	console.log("populating table");
	var movieTableBodyElement = jQuery("#shopping_cart_table_body");
	
	for (var i = 0; i < resultData.length; i++) {
		var rowHTML = "";
		rowHTML += "<tr>";
		//rowHTML += "<td>" + resultData[i]["m_id"] + "</td>";
		rowHTML += "<td>" + resultData[i]["m_quantity"] + "</td>";
		rowHTML += "<td><a href=\"singlemovie.html\?title=" + resultData[i]["m_title"] + "\">" + resultData[i]["m_title"] + "</a></td>";
		//rowHTML += "<td>" + resultData[i]["m_year"] + "</td>";
		//rowHTML += "<td>" + resultData[i]["m_director"] + "</td>";
		//rowHTML += "<td>" + resultData[i]["m_genres"] + "</td>";
		//rowHTML += "<td>" + resultData[i]["m_actors"] + "</td>";
		rowHTML += "</tr>";
		movieTableBodyElement.append(rowHTML);
	}
}

$.ajax({
	dataType: "json",
	method: "GET",
	url: "/project2/Checkout",
	success: (resultData) => showShoppingCart(resultData)
});

$("#payment_button").click(handlePayment);