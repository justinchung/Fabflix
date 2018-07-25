
/** 
 * This function redirects the user to movielist.html with the appropriate
 *  parameters in the URL as a query string
 */
function handleSearchRedirect() {
	//formSubmitEvent.preventDefault();
	var redirect_url = "/project2/movielist.html?";
	
	var form = document.forms["search_form"];
	for (var j = 0; j < 4; j++) {
		redirect_url += form.elements[j].name + "=" + form.elements[j].value;
		if (j < 3) {
			redirect_url += "&";
		}
	}
	redirect_url += "&genre=&type=search";
	console.log("redirecting to " + redirect_url);
	
	window.location.replace(redirect_url);
}

/** 
 * Extracts the user-inputted data from the form
 */
function submitSearchForm(formSubmitEvent) {
	console.log("clearing search_table_body of previous rows");
	$("#search_table_body tr").remove();
	
	console.log("submit movie search form");
	
	formSubmitEvent.preventDefault();
	
	jQuery.get("/project2/MovieSearch", 
			jQuery("#search_form").serialize(), 
			(resultDataString) => handleSearchRedirect(resultDataString));
}

//bind the submit action of the form to a handler function
//jQuery("#search_form").submit((event) => submitSearchForm(event));

$("#submit_search").click(handleSearchRedirect);

//$(document).ready(function() {
//	$("#search_form").submit(handleSearchRedirect());
//});
