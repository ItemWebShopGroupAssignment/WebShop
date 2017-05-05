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
		.otherwise({ redirectTo : "/" });
	}
	
	var homeController = function($scope) {
		$scope.title = "Hello World!";
	};
	
	// Bind the config and controllers to the aplication.
	app.config(["$routeProvider", config]);
	app.controller("homeController", ["$scope", homeController]);
	
}())