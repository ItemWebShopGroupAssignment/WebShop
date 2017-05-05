/**
 * Author: Gustaf Peter Hultgren
 */

(function() {
	
	var app = angular.module("application", ["ngRoute"]);
	
	var config = function($routeProvider) {
		$routeProvider.when("/", {
			templateUrl : "home.html",
			controller : "homeController"
		})
		.when("/cart", {
			templateUrl : "cart.html",
			controller : "cartController"
		})
		.when("/browse", {
			templateUrl : "browse.html",
			controller : "browseController"
		})
		.otherwise({ redirectTo : "/" });
	}
	
	var homeController = function($scope) {
		$scope.title = "Hello World!";
	};
	
	// Bind the config and controllers to the aplication.
	app.config(["$routeProvider", config]);
	app.controller("homeController", ["$scope", homeController]);
	
}())