HomePageCtrl
/*Angluar controller*/
angular.module('homepage', ['ngCookies']).
  config(function($routeProvider) {
    $routeProvider.
      when('/', {controller:HomePageCtrl, templateUrl:'partials/home/home.html'}).
      when('/getstarted', {controller:HomePageCtrl, templateUrl:'partials/home/getstarted.html'}).
      when('/news', {controller:HomePageCtrl, templateUrl:'partials/home/news.html'}).
	  when('/about', {controller:HomePageCtrl, templateUrl:'partials/home/about.html'}).
      otherwise({redirectTo:'/'});
  });

function HomePageCtrl($scope, $http, $window, $location, $cookies) {

	$scope.doGplusAuth = function() {
    	$window.location.href = './services/user/googleauth';
    }
}