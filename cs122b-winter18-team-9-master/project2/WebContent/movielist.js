
/**
 * Extracts the query string from URL and return the value of
 * the specified parameter
 */
function getQueryVariable(variable) {
	console.log("parsing query string");
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

/**
 * Fills the movie list table
 */
function handleSearchResult(resultData) {	
	console.log("populating movielist table");
	$("#movielist_table_body tr").remove();
	// populate the star table
	var movieTableBodyElement = jQuery("#movielist_table_body");
	for (var i = 0; i < resultData.length; i++) {
		var rowHTML = "";
		rowHTML += "<tr>";
		rowHTML += "<td>" + resultData[i]["m_id"] + "</td>";
		
		// Each movie hyperlinked to its own singlemove.html
		rowHTML += "<td><a href=\"singlemovie.html\?title=" + resultData[i]["m_title"] + "\">" + resultData[i]["m_title"] + "</a></td>";
		rowHTML += "<td>" + resultData[i]["m_year"] + "</td>";
		rowHTML += "<td>" + resultData[i]["m_director"] + "</td>";
		rowHTML += "<td>" + resultData[i]["m_genres"] + "</td>";
		
		
		// Each actor hyperlinked to their own singlestar.html
		rowHTML += "<td class=\"parent\"><table><tr>";
		var actors = resultData[i]["m_actors"].split(",");
		for (var j = 0; j < actors.length; j++) {
			rowHTML += "<td><a href=\"/project2/singlestar.html?name=" + actors[j] + "\">" + actors[j] + "</a></td>";
		}
		rowHTML += "</tr></table></td>";
		
		
		rowHTML += "</tr>";
		movieTableBodyElement.append(rowHTML);
	}
	$(document).ready(function() {
		console.log("paginating");
	    $('#movielist_table').DataTable( {
	        "pagingType": "full_numbers"
	    } );
	} );
}

// HTTP GET request to MovieSearch (backend), which queries the database
// On success: pass resultData to function handleSearchResult to fill tables
$.ajax({
	data: {
		"title": getQueryVariable("title"),
		"year": getQueryVariable("year"),
		"director": getQueryVariable("director"),
		"actor": getQueryVariable("actor"),
		"genre": getQueryVariable("genre"),
		"type": getQueryVariable("type")
	},
	dataType: "json",
	method: "GET",
	url: "/project2/MovieSearch",
	success: (resultData) => handleSearchResult(resultData),
	/*error: function(jqXHR, textStatus, errorThrown) { 
		console.log(jqXHR.status);
		console.log(textStatus);
		console.log(errorThrown); }*/
});
