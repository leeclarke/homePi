/*Angluar controller*/
function DashCtrl($scope, $http, $location) {
	init($scope, $location)
	//TODO: Retrieve the User Profile.

	//TODO: Save this as a cookie.


	function init($scope, $location){
		//tried using $location.search() for this but it was always empty... ran out of time messing with it so just wrote my owe, it will get fixed one day...
		var queryString = $location.absUrl().match(/^[^?]+\??([^#]*).*$/)[1];
		console.log('queryString='+queryString);
		pairs = queryString.split("&")
		params = [];
		angular.forEach(pairs , function(kvp){
			pair = kvp.split('=');
			params[pair[0]] = pair[1];
		}); 

		$scope.userId = params['user_id'];
		$scope.auth_token = params['auth_token'];
	}
  	

}