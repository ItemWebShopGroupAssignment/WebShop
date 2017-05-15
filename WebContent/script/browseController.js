/**
 *  Browse Controller.
 */

(function() {
	
	var app = angular.module("application");
	
	var browseController = function($scope, $http, $timeout) {
		$scope.title = "";

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
			 $scope.title = response.data;
			 $timeout(function() {
				 $scope.title = "";
			 }, 3000);
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
	
	app.controller("browseController", [ "$scope", "$http", "$timeout", browseController ]);
	
}())
