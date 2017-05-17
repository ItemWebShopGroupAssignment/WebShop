/**
 * Angular Routing.
 */

(function() {
	
	var app = angular.module("application", [ "ngRoute" ]);

	var checkUser = function($q, $rootScope, $location, $http) {
		
		var validUser = false;
		
		var onSuccess = function(response) {
			var user = response.data;
			
			var deferred = $q.defer();
			
			if(user !== "") {
				$rootScope.userProfile = response.data;
				deferred.resolve = true
				$location.path("/admin");
			}
			else {
				$location.path("/login");
			}
			
			validUser = deferred.resolve;
		}
		
		var onError = function(reason) {
			deferred.reject();
			$location.path("/");
		}
		
		$http.post("LoadUserProfile").then(onSuccess, onError);
		
		return validUser;
	}
	
	var config = function($routeProvider) {
		$routeProvider.when("/", {
			templateUrl : "home.html",
			controller : "homeController"
		}).when("/cart", {
			templateUrl : "cart.html",
			controller : "cartController"
		}).when("/browse", {
			templateUrl : "browse.html",
			controller : "browseController"
		}).when("/admin", {
			templateUrl : "admin.html",
			controller : "adminController",
			
			resolve: {
				factory: checkUser
			}
		}).when("/login", {
			templateUrl : "login.html",
			controller : "loginController"
		}).otherwise({
			redirectTo : "/"
		});
	};

	// Bind the config and controllers to the application.
	app.config([ "$routeProvider", config ]);

}())