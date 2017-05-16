/**
 * Admin Controller.
 */

(function() {
	
	var app = angular.module("application");
	
	var adminController = function($scope, $http) {
		$scope.title = "Items";
		
		var categories = [];
		$scope.categories = categories;
		
		var items = [];
		$scope.items = items;
		
		$scope.inStock = "Hm";
		
		$scope.removeFromInventory = function(index) {

			// här tar vi bort spelet från listan:
			$scope.items.splice(index, 1);

			// Det som återstår är att ta bort spelet i databasen,
			// och tillhörande funktionalitet för det.
		};
		
		var onIncreaseStockBalanceComplete = function() {
			$http.get("GetItems").then(onItemsComplete, onError);
		}
		
		var onDecreaseStockBalanceComplete = function() {
			$http.get("GetItems").then(onItemsComplete, onError);
		}
		
		var onCategoriesComplete = function(response) {
			$scope.categories = response.data;
		}

		var onItemsComplete = function(response) {
			$scope.items = response.data;
		}


		var onError = function(reason) {
			$scope.error = "Could not fetch data " + reason.status;
		}
		
		$scope.addToInventory = function(artNr, count) {
			var parameters = {
					'artNr' : artNr,
					'stockBalance' : count
			};
			
			var jsonPackage = JSON.stringify(parameters);
			
			$http.post("IncreaseStockBalance", jsonPackage).then(onIncreaseStockBalanceComplete, onError);
		}
		
		$scope.removeFromInventory = function(artNr, count) {
			var parameters = {
					'artNr' : artNr,
					'stockBalance' : count
			};
			
			var jsonPackage = JSON.stringify(parameters);
			
			$http.post("DecreaseStockBalance", jsonPackage).then(onDecreaseStockBalanceComplete, onError);
		}
		
		$http.get("GetCategories").then(onCategoriesComplete, onError);

		$http.get("GetItems").then(onItemsComplete, onError);

		$scope.viewDescription = function(index) {
			$scope.selectedItem = $scope.items[index];

		}

	};
	
	app.controller("adminController", [ "$scope", "$http", adminController ]);
	
}())