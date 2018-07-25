
function displayMetadata(resultData) {
	console.log("populating table");
	
	// Populate table
	var metadataTableBodyElement = jQuery("#metadata_table_body");
	for (var i = 0; i < resultData.length; i++) {
		var rowHTML = "";
		rowHTML += "<tr>";
		rowHTML += "<td>" + resultData[i]["table_name"] + "</td>";
		rowHTML += "<td>" + resultData[i]["schema"] + "</td>";
		rowHTML += "</tr>";
		
		metadataTableBodyElement.append(rowHTML);
	}
}

$.ajax({
	dataType: "json",
	method: "GET",
	url: "/project2/Dashboard",
	success: (resultData) => displayMetadata(resultData),
	error: function(jqXHR, textStatus, errorThrown) { 
		console.log(jqXHR.status);
		console.log(textStatus);
		console.log(errorThrown); 
		alert("fail");
	}
})