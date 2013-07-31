/*Angluar controller*/
angular.module('dashboard', ['ngCookies','userServices']).
  config(function($routeProvider) {
    $routeProvider.
      when('/', {controller:DashCtrl, templateUrl:'partials/dash.html'}).
      when('/social', {controller:SearchCtrl, templateUrl:'partials/search.html'}).
      when('/news', {controller:NewsCtrl, templateUrl:'partials/news.html'}).
	  when('/profile', {controller:ProfileCtrl, templateUrl:'partials/profile.html'}).
      when('/profile/:user_name', {controller:ProfileViewCtrl, templateUrl:'partials/viewprofile.html'}).
      when('/piprofile/:piId', {controller:ProfileCtrl, templateUrl:'partials/piprofile.html'}).
      when('/apps/:appId', {controller:AppCtrl, templateUrl:'partials/apps.html'}).
      when('/noprofile', {controller:ProfileCtrl, templateUrl:'partials/no-profile.html'}).
      otherwise({redirectTo:'/'});
  });

function HomeCtrl($scope, $http, $window, $location, $cookies, User) {

	params  = Util.parseQueryString($scope, $location)
	if(params['auth_token']){
		console.log('uid:'+ params['user_id']);
		console.log('token:'+ params['auth_token']);
		
		//Add token to the header
		if(!($http.defaults.headers.get)){
			$http.defaults.headers.get = {};	
		} 
		$http.defaults.headers.get.access_token = params['auth_token'];
		
		//Save access token as a cookie.
		$cookies.access_token = params['auth_token'];
		$cookies.user_id = params['user_id'];
		$window.location.replace('/home.html');
	} else {
		if($cookies.access_token) {
			if(!($http.defaults.headers.get)){
				$http.defaults.headers.get = {};	
			} 
			$http.defaults.headers.get.access_token = $cookies.access_token;
			$scope.user = User.get({user_name: $cookies.user_id});
		} 
	}

	//TODO: Also test for direct deep links.
}

function DashCtrl($scope, $http, $location, $cookies, User) {
	
  	console.log('cookie='+$cookies.access_token);
	if(!$scope.user) {
		//no user auth, show options.
		$location.path('/noprofile');
	}
}

function SearchCtrl($scope, $http, $location, $cookies, User) {

}

function NewsCtrl($scope, $http, $location, $cookies, User) {

}

function ProfileViewCtrl($scope, $http, $location, $routeParams, $cookies, User) {

	if($routeParams.user_name || $routeParams.piId){
		console.log('user_name=' + $routeParams.user_name);
		$scope.viewuser = User.getByName({user_name: $routeParams.user_name});
		if($scope.viewuser){
			
		}
	}
}


function ProfileCtrl($scope, $http, $location, $cookies, User) {

	if(!$scope.user){
		$location.path('/noprofile');
	}

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

function AppCtrl($scope, $http, $location, $routeParams, $cookies, User) {

	if($routeParams.appId){
		if($routeParams.appId == 'new'){
			$scope.userApp = {edit: 'true', updateTime: 'Right Now!', createTime: 'Right Now!'};
			$scope.viewuser = $scope.user;
		} else{
			//seach User for the app and add edit true/ insert into scope.
			for( i in $scope.user.managedApps){
				if($scope.user.managedApps[i].appId == $routeParams.appId){
					$scope.userApp = $scope.user.managedApps[i];
					$scope.viewuser = $scope.user;
					return;
				}
			}
			//if get to here then this is a readonly, make call to backend


		}
	} else{
		console.log('Error, missing appId');
	}

}