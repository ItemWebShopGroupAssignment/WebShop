/**
 * Cart Item Info Controller.
 */

(function() {
	
	var app = angular.module("application");
	
	var cartiteminfoController = function($scope, $http, $routeParams){
		$scope.title = "CartItemInfo";
	};
	
	app.controller("cartiteminfoController", ["$scope", "$http","$routeParams", cartiteminfoController]);
	
}())