/*Angluar controller*/
angular.module('dashboard', ['ngCookies','userServices']).
  config(function($routeProvider) {
    $routeProvider.
      when('/', {controller:DashCtrl, templateUrl:'partials/dash.html'}).
      when('/social', {controller:SearchCtrl, templateUrl:'partials/search.html'}).
      when('/news', {controller:NewsCtrl, templateUrl:'partials/news.html'}).
	  when('/profile', {controller:ProfileCtrl, templateUrl:'partials/profile.html'}).
      when('/profile/:user_name', {controller:ProfileCtrl, templateUrl:'partials/profile.html'}).
      otherwise({redirectTo:'/'});
  });

function HomeCtrl($scope) {

}

function DashCtrl($scope, $http, $location, $cookies, User) {
	init($scope, $location)
	//TODO: Retrieve the User Profile.
	console.log('uid'+ $scope.user_name);
	console.log('token'+ $scope.access_token);
	
	//Add token to the header
	if(!($http.defaults.headers.get)){
		$http.defaults.headers.get = {};	
	}
	$http.defaults.headers.get.access_token = $scope.access_token;

	$scope.user = User.get({user_name: $scope.user_name});
	
	//TODO: Save this as a cookie.
	$cookies.access_token = $scope.access_token;

	//Check for fail and redirect to login/index

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

		$scope.user_name = params['user_name'];
		$scope.access_token = params['auth_token'];
		
	}
  	

}

function SearchCtrl($scope, $http, $location, $cookies, User) {

}

function NewsCtrl($scope, $http, $location, $cookies, User) {

}

function ProfileCtrl($scope, $http, $location, $cookies, User) {

}