'use strict';

/* Services */

angular.module('userServices', ['ngResource']).
    factory('User', function($resource){
  return $resource('/services/user/profile/:user_name', {}, {
  	get: {method:'GET', params:{user_name:'user_name'}, isArray:false, headers: {'access_token':'@access_token'}},
    query: {method:'GET', params:{user_name:'user_name'}, isArray:false}
  });
});