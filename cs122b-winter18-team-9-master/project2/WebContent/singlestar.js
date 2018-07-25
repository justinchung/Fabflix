
function getQueryVariable(variable) {
    var query_string = window.location.search.substring(1);
    var vars = query_string.split('&');
    
    for (var i = 0; i < vars.length; i++) {
        var pair = vars[i].split('=');
        if (decodeURIComponent(pair[0]) == variable) {
            return decodeURIComponent(pair[1]);
        }
    }
    console.log('Query variable %s not found', variable);
}

function loadTable(resultData) {
	console.log("populating table");
	var movieTableBodyElement = jQuery("#single_star_table_body");
	var rowHTML = "";
	rowHTML += "<tr>";
	rowHTML += "<td>" + resultData[0]["s_name"] + "</td>";
	rowHTML += "<td>" + resultData[0]["s_dob"] + "</td>";
	
	var movies = resultData[0]["s_movies"].split(",");
	for (var i = 0; i < movies.length; i++) {
		rowHTML += "<td><a href=\"/project2/singlemovie.html?title=" + movies[i] + "\">" + movies[i] + "</a></td>";
	}
	//rowHTML += "<td>" + resultData[0]["s_movies"] + "</td>";
	rowHTML += "</tr>";
	
	movieTableBodyElement.append(rowHTML);
	console.log(resultData)
	console.log(rowHTML)
}


$.ajax({
	data: {
		name: getQueryVariable("name"),
	},
	dataType: "json",
	method: "GET",
	url: "/project2/SingleStar",
	success: (resultData) => loadTable(resultData)
});