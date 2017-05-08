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
	};
	
	var homeController = function($scope) {
		
	};
	
	var cartController = function($scope, $http) {
		$http.get("GetCartItems?cartId=1").then(onGetCartComplete, onCartError);
		var onGetCartComplete = function(response){ 
			$scope.items = response.data;
		
		}
		var onCartError = function(reason){
			$scope.error = reason.status;
		}
	};
	
	var browseController = function($scope, $http) {
//		routingApplication.controller("booksController", function($scope, $http) {
			$scope.title = "Items";
			
			var items = [];
			$scope.items = items;

			$scope.removeFromInventory = function(index) {
				
				//här tar vi bort spelet från listan:
				$scope.items.splice(index, 1); 
				
				//Det som återstår är att ta bort spelet i databasen, 
				//och tillhörande funktionalitet för det.
			};

			var onItemsComplete = function(response) {
				$scope.items = response.data;
			}

//			var onAddItemComplete = function(response) {
//				$scope.description = "Added new game to list " + response.data;
//				
//				//här lägger vi till spelet i listan:
//				var tmp = angular.copy(item);
//				$scope.items.push(tmp);
//				$scope.item.title = "";
//				$scope.item.author = "";
//
//			}

			var onError = function(reason) {
				$scope.error = "Could not fetch data " + reason.status;
			}

			$http.get("GetItems").then(onItemsComplete, onError);
	
		
	};
	
	// Bind the config and controllers to the aplication.
	app.config(["$routeProvider", config]);
	app.controller("homeController", ["$scope", homeController]);
	app.controller("cartController", ["$scope", cartController]);
	app.controller("browseController", ["$scope", "$http", browseController]);
	
}())