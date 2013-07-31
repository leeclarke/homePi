Util = function(){

}

Util.parseQueryString = function($scope, $location){
	if(console){
		console.log('parse url params');
	}
	//tried using $location.search() for this but it was always empty... ran out of time messing with it so just wrote my own, it will get fixed one day...
	var queryString = $location.absUrl().match(/^[^?]+\??([^#]*).*$/)[1];
	console.log('queryString='+queryString);
	pairs = queryString.split("&")
	params = [];
	angular.forEach(pairs , function(kvp){
		pair = kvp.split('=');
		params[pair[0]] = pair[1];
	}); 
	
	//TODO: Cleanup url
	$location.search('auth_token', null).replace();
	$location.search('user_name',null);
	return params; 
}