
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
	var movieTableBodyElement = jQuery("#single_movie_table_body");
	var rowHTML = "";
	rowHTML += "<tr>";
	rowHTML += "<td id=movieId>" + resultData[0]["m_id"] + "</td>";
	rowHTML += "<td>" + resultData[0]["m_title"] + "</td>";
	rowHTML += "<td>" + resultData[0]["m_year"] + "</td>";
	rowHTML += "<td>" + resultData[0]["m_director"] + "</td>";
	
	// Each genre gets its own <td>
	rowHTML += "<td class=\"parent\"><table><tr>";
	var genres = resultData[0]["m_genres"].split(",");
	for (var i = 0; i < genres.length; i++) {
		rowHTML += "<td><a href=\"/project2/movielist.html?title=&year=&director=&actor=&genre=" + genres[i] + "&type=browse\">"+ genres[i] + "</a></td>"
	}
	rowHTML += "</tr></table></td>";
	
	// Each actor gets its own <td>
	rowHTML += "<td class=\"parent\"><table><tr>";
	var actors = resultData[0]["m_actors"].split(",");
	for (var i = 0; i < actors.length; i++) {
		rowHTML += "<td><a href=\"/project2/singlestar.html?name=" + actors[i] + "\">" + actors[i] + "</a></td>";
	}
	rowHTML += "</tr></table></td>";

	//rowHTML += "<td>" + resultData[0]["m_actors"] + "</td>";
	rowHTML += "</tr>";
	movieTableBodyElement.append(rowHTML);
	
}

$.ajax({
	data: {
		title: getQueryVariable("title"),
		year: "",
		director: "",
		actor: "",
		genre: "",
		type: "search"
	},
	dataType: "json",
	method: "GET",
	url: "/project2/MovieSearch",
	success: (resultData) => loadTable(resultData)
});