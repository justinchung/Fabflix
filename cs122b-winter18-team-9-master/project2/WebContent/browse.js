
function handleBrowseTitleRedirect() {
	var redirect_url = "/project2/movielist.html?title=";
	
	var dropmenu = document.getElementById("titledrop");
	var query_param = dropmenu.value;
	
	redirect_url += query_param;
		
	redirect_url += "&year=&director=&actor=&genre=&type=browse";
	
	console.log("redirecting to " + redirect_url);
	
	window.location.replace(redirect_url);
}


function handleBrowseGenreRedirect() {
	var redirect_url = "/project2/movielist.html?title=&year=&director=&actor=&genre=";
	
	var dropmenu = document.getElementById("genredrop");
	var query_param = dropmenu.value;
	redirect_url += query_param;
	
	
	redirect_url += "&type=browse";
	
	console.log("redirecting to " + redirect_url);
	
	window.location.replace(redirect_url);
}

$("#submit_browse_title").click(handleBrowseTitleRedirect);
$("#submit_browse_genre").click(handleBrowseGenreRedirect);
