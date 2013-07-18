/*Angluar controller*/
angular.module('dashboard', ['ngCookies','userServices']).
  config(function($routeProvider) {
    $routeProvider.
      when('/', {controller:DashCtrl, templateUrl:'partials/dash.html'}).
      when('/social', {controller:SearchCtrl, templateUrl:'partials/search.html'}).
      when('/news', {controller:NewsCtrl, templateUrl:'partials/news.html'}).
	  when('/profile', {controller:ProfileCtrl, templateUrl:'partials/profile.html'}).
      when('/profile/:user_name', {controller:ProfileCtrl, templateUrl:'partials/profile.html'}).
      when('/piprofile/:piId', {controller:ProfileCtrl, templateUrl:'partials/piprofile.html'}).
      otherwise({redirectTo:'/'});
  });

function HomeCtrl($scope, $http, $window, $location, $cookies, User) {
	console.log('In Home controller');

	params  = Util.parseQueryString($scope, $location)
	if(params.length >0){
		//clear query params (Doesnt do anything)
		$location.path('/');
		wondow.location.href = '/home.html';
	}

	console.log('uid:'+ params['user_id']);
	console.log('token:'+ params['auth_token']);
	
	//Add token to the header
	if(!($http.defaults.headers.get)){
		$http.defaults.headers.get = {};	
	} 
	$http.defaults.headers.get.access_token = params['auth_token'];

	$scope.user = User.get({user_name: params['user_id']});
	
	//Save access token as a cookie.
	$cookies.access_token = params['auth_token'];

	//TODO: Check for fail and redirect to login/index

}

function DashCtrl($scope, $http, $location, $cookies, User) {
	
  	console.log('cookie='+$cookies.access_token);

}

function SearchCtrl($scope, $http, $location, $cookies, User) {

}

function NewsCtrl($scope, $http, $location, $cookies, User) {

}

function ProfileCtrl($scope, $http, $location, $cookies, User) {

	$scope.saveuser = function() {
      console.log('cookie_token='+$cookies.access_token);
      $http.defaults.headers.get.access_token = $cookies.access_token;
      $scope.user.$save();
      $location.path('/profile');
    };

	$scope.isClean = function() {
      return angular.equals($scope.remote, $scope.user);
    }
}