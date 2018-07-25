
var cache = new Map();

/*
 * This function is called by the library when it needs to lookup a query.
 * 
 * The parameter query is the query string.
 * The doneCallback is a callback function provided by the library, after you get the
 *   suggestion list from AJAX, you need to call this function to let the library know.
 */
function handleLookup(query, doneCallback) {
	console.log("autocomplete initiated")
	console.log("sending AJAX request to backend Java Servlet")
		
	if (cache.get(query)) {
		console.log("Fetching result from cache");
		handleLookupAjaxSuccess(cache.get(query), query, doneCallback);
	}
	else {
	// sending the HTTP GET request to the Java Servlet endpoint hero-suggestion
	// with the query data
		jQuery.ajax({
			dataType: "json",
			method: "GET",
			// generate the request url from the query.
			// escape the query string to avoid errors caused by special characters 
			url: "Autocomplete?query=" + escape(query),
			success: function(data) {
				// pass the data, query, and doneCallback function into the success handler
				handleLookupAjaxSuccess(data, query, doneCallback) 
			},
			error: function(errorData) {
				console.log("lookup ajax error")
				console.log(errorData)
			}
		})
	}
}


/*
 * This function is used to handle the ajax success callback function.
 * It is called by our own code upon the success of the AJAX request
 * 
 * data is the JSON data string you get from your Java Servlet
 * 
 */
function handleLookupAjaxSuccess(data, query, doneCallback) {
	console.log("lookup ajax successful")
	console.log(data)
	
	if (!cache.get(query)) {
		console.log("Results for query: " + query + " cached");
		cache.set(query, data)
	}
	// call the callback function provided by the autocomplete library
	// add "{suggestions: jsonData}" to satisfy the library response format according to
	//   the "Response Format" section in documentation
	doneCallback( { suggestions: data } );
}


/*
 * This function is the select suggestion hanlder function. 
 * When a suggestion is selected, this function is called by the library.
 * 
 * You can redirect to the page you want using the suggestion data.
 */
function handleSelectSuggestion(suggestion) {
	// TODO: jump to the specific result page based on the selected suggestion
	console.log("you select " + suggestion["value"])
	
	if (suggestion["data"]["category"] == "Movies") {
		var redirect_url = "/project2/singlemovie.html?title="
		redirect_url += suggestion["value"];
	}
	
	else if (suggestion["data"]["category"] == "Stars") {
		var redirect_url = "/project2/singlestar.html?name=";
		redirect_url += suggestion["value"];
	}
	
	console.log("redirecting to " + redirect_url);	
	window.location.replace(redirect_url);
}


/*
 * This statement binds the autocomplete library with the input box element and 
 *   sets necessary parameters of the library.
 * 
 * The library documentation can be find here: 
 *   https://github.com/devbridge/jQuery-Autocomplete
 *   https://www.devbridge.com/sourcery/components/jquery-autocomplete/
 * 
 */
// $('#autocomplete') is to find element by the ID "autocomplete"
$('#search_bar').autocomplete({
	// documentation of the lookup function can be found under the "Custom lookup function" section
    lookup: function (query, doneCallback) {
    		handleLookup(query, doneCallback)
    },
    onSelect: function(suggestion) {
    		handleSelectSuggestion(suggestion)
    },
    // set the groupby name in the response json data field
    groupBy: "category",
    // set delay time
    deferRequestBy: 300,
    minChars: 3,
});


/*
 * do normal full text search if no suggestion is selected 
 */
function handleNormalSearch(query) {
	console.log("doing normal search with query: " + query);	
		
	var redirect_url = "/project2/movielist.html?title=";
	redirect_url += query
	redirect_url += "&year=&director=&actor=&genre=&type=searchbar";
	console.log("redirecting to " + redirect_url);
		
	window.location.replace(redirect_url);
}

// bind pressing enter key to a handler function
$('#search_bar').keypress(function(event) {
	// keyCode 13 is the enter key
	if (event.keyCode == 13) {
		// pass the value of the input box to the handler function
		handleNormalSearch($('#search_bar').val())
	}
})
