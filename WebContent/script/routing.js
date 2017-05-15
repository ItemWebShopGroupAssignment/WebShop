/**
 * Angular Routing.
 */

(function() {
	
	var app = angular.module("application", [ "ngRoute" ]);

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
			controller : "adminController"
		}).when ("/cartiteminfo/:artNr", {
			templateUrl : "cartiteminfo.html",
			controller : "cartiteminfoController"
		}).otherwise({
			redirectTo : "/"
		});
	};

	// Bind the config and controllers to the application.
	app.config([ "$routeProvider", config ]);

}())