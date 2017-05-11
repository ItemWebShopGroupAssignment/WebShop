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
		}).otherwise({
			redirectTo : "/"
		});
	};

	var homeController = function($scope) {

	};

	var cartController = function($scope, $http) {

		var onGetCartComplete = function(response) {

			$scope.items = response.data;

		}
		var onCartError = function(reason) {
			$scope.Test = "error";
			$scope.error = reason.status;
		}

		$http.get("GetCartItems?cartId=1").then(onGetCartComplete, onCartError);

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
		 $scope.description = "Added new game to cart " + response.data;
						
		 //här lägger vi till spelet i shopping carten:
		 var tmp = angular.copy(item);
		 $scope.items.push(tmp);
		 $scope.item.artNr = "";
		 $scope.item.cartId = "1";
		 $scope.item.stockBalance = "";
		
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
				var uri = "AddToCart2?artNr=" + artNr + "&count=" + count + "&cartId=" + 1;
				alert(uri);
				$http.get(uri).then(onAddToCartComplete, onError);
			}
		}

	};

	// Bind the config and controllers to the aplication.
	app.config([ "$routeProvider", config ]);
	app.controller("homeController", [ "$scope", homeController ]);
	app.controller("cartController", [ "$scope", "$http", cartController ]);
	app.controller("browseController", [ "$scope", "$http", browseController ]);

}())