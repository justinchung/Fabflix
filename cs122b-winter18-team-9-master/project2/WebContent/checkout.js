
function handleCheckoutRedirect() {
	var redirect_url = "/project2/shoppingcart.html";
	
	console.log("redirecting to " + redirect_url);
	
	window.location.replace(redirect_url);
}

$("#checkout_button").click(handleCheckoutRedirect);