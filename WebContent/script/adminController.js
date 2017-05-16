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
		
		$scope.show = false;
		$scope.addOrEdit = "Add game";
		
		$scope.removeFromInventory = function(index) {

			// här tar vi bort spelet från listan:
			$scope.items.splice(index, 1);

			// Det som återstår är att ta bort spelet i databasen,
			// och tillhörande funktionalitet för det.
		};
		
		$scope.showAddForm = function() {
			if ($scope.show == false) {
				$scope.show = true;
				$scope.addOrEdit = "Edit game";
			}
			else {
				$scope.show = false;
				$scope.addOrEdit = "Add game";
			}
		}
		
		var onIncreaseStockBalanceComplete = function(response) {
			$http.get("GetItems").then(onItemsComplete, onError);
		}
		
		var onDecreaseStockBalanceComplete = function(response) {
			$http.get("GetItems").then(onItemsComplete, onError);
		}
		
		var onCategoriesComplete = function(response) {
			$scope.categories = response.data;
		}

		var onItemsComplete = function(response) {
			$scope.items = response.data;
		}
		
		var onDeleteComplete = function(response) {
			$http.get("GetItems").then(onItemsComplete, onError);
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
		
		$scope.deleteFromInventory = function(artNr) {
			var parameters = {
					'artNr' : artNr
			};
			
			var jsonPackage = JSON.stringify(parameters);
			
			$http.post("RemoveFromInventory", jsonPackage).then(onDeleteComplete, onError);
		}
		
		$http.get("GetCategories").then(onCategoriesComplete, onError);

		$http.get("GetItems").then(onItemsComplete, onError);

		$scope.viewDescription = function(index) {
			$scope.selectedItem = $scope.items[index];

		}

	};
	
	app.controller("adminController", [ "$scope", "$http", adminController ]);
	
}())