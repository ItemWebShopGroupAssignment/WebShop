/**
 * Author: Gustaf Peter Hultgren
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
		}).when ("/cartiteminfo", {
			templateUrl : "cartiteminfo.html",
			controller : "cartiteminfoController"
		}).otherwise({
			redirectTo : "/"
		});
	};

	var homeController = function($scope) {
		
	};

	var cartController = function($scope, $http) {

		$scope.amount = 1;
		var onGetCartComplete = function(response) {

			$scope.items = response.data;
			
			$scope.totalCost = 0;
			for(var i = 0; i < $scope.items.length; i++) {
				$scope.totalCost += ($scope.items[i].price * $scope.items[i].stockBalance);
			}

		}
		
		var onCartError = function(reason) {
			$scope.Test = "error";
			$scope.error = reason.status;
		}

		$http.get("GetCartItems").then(onGetCartComplete, onCartError);

		var onRemoveComplete = function(response) {
			alert(response.data);
			$http.get("GetCartItems?").then(onGetCartComplete, onCartError); 
		}
		
		$scope.removeFromCart = function(artNr, count){
			var parameters = {
					'artNr' : artNr,
					'stockBalance' : count
			};
			
			var jsonParameters = JSON.stringify(parameters);
			
			$http.post("RemoveFromCart", jsonParameters).then(onRemoveComplete, onCartError);
		}
		
		$scope.dumpCart = function() {
			 
		}
		
		var onCheckoutComplete = function(response) {
			alert(response.data);
		}
		
		$scope.checkoutCart = function() {
			
			$http.post("CheckoutCart").then(onCheckoutComplete, onCartError);
		}
	};

	var browseController = function($scope, $http) {
		$scope.title = "Items";

		var items = [];
		$scope.items = items;
		
		$scope.inStock = "Hm";

		$scope.removeFromInventory = function(index) {

			// här tar vi bort spelet från listan:
			$scope.items.splice(index, 1);

			// Det som återstår är att ta bort spelet i databasen,
			// och tillhörande funktionalitet för det.
		};

		var onItemsComplete = function(response) {
			$scope.items = response.data;
		}

		 var onAddToCartComplete = function(response) {
			 alert(response.data);
		
		 }

		var onError = function(reason) {
			$scope.error = "Could not fetch data " + reason.status;
		}

		$http.get("GetItems").then(onItemsComplete, onError);

		$scope.viewDescription = function(index) {
			$scope.selectedItem = $scope.items[index];

		}
		
		$scope.addToCart = function(artNr, count, stockBalance) {			
			if (stockBalance <= 0) {
				alert("Not in stock!");
			}
			else {
				var parameters = {
						'artNr' : artNr,
						'stockBalance' : count
				};
				
				var jsonPackage = JSON.stringify(parameters);
				
				$http.post("AddToCart", jsonPackage).then(onAddToCartComplete, onError);
			}
		}

	};
	
	var adminController = function($scope, $http) {
		$scope.title = "Items";

		var items = [];
		$scope.items = items;
		
		$scope.inStock = "Hm";

		$scope.removeFromInventory = function(index) {

			// här tar vi bort spelet från listan:
			$scope.items.splice(index, 1);

			// Det som återstår är att ta bort spelet i databasen,
			// och tillhörande funktionalitet för det.
		};

		var onItemsComplete = function(response) {
			$scope.items = response.data;
		}


		var onError = function(reason) {
			$scope.error = "Could not fetch data " + reason.status;
		}

		$http.get("GetItems").then(onItemsComplete, onError);

		$scope.viewDescription = function(index) {
			$scope.selectedItem = $scope.items[index];

		}

	};
	
	var cartiteminfoController = function($scope, $http){
		$scope.title = "CartItemInfo";
	};

	// Bind the config and controllers to the application.
	app.config([ "$routeProvider", config ]);
	app.controller("homeController", [ "$scope", homeController ]);
	app.controller("cartController", [ "$scope", "$http", cartController ]);
	app.controller("browseController", [ "$scope", "$http", browseController ]);
	app.controller("adminController", [ "$scope", "$http", adminController ]);
	app.controller("cartiteminfoController", ["$scope", "$http", cartiteminfoController]);

}())