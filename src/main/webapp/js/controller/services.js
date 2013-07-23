'use strict';

/* Services */

angular.module('userServices', ['ngResource']).
    factory('User', function($resource){
  		return $resource('/services/user/profile/:user_name', {}, {
  			get: {method:'GET', params:{user_name:'user_id', key:'id'}, isArray:false, headers: {'access_token':'@access_token'}},
  			getByName: {method:'GET', params:{user_name:'user_nameid'}, isArray:false, headers: {'access_token':'@access_token'}},
  			save: {method:'POST', params:{user_name:'@userId'}, isArray:false, headers: {'access_token':'@googleAuthToken'}},
    		query: {method:'GET', params:{user_name:'user_name'}, isArray:false}
  	});
});