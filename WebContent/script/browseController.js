/**
 *  Browse Controller.
 */

(function() {
	
	var app = angular.module("application");
	
	var browseController = function($scope, $http, $timeout) {
		$scope.title = "";

		var items = [];
		$scope.items = items;

		var onItemsComplete = function(response) {
			$scope.items = response.data;
		}

		 var onAddToCartComplete = function(response) {
			 $scope.title = response.data;
			 
			 $timeout(function() {
				 $scope.title = "";
			 }, 3000);
			 
			 $http.get("GetItems").then(onItemsComplete, onError);

		 }

		var onError = function(reason) {
			alert("Could not fetch data " + reason.status);
		}

		$http.get("GetItems").then(onItemsComplete, onError);

		$scope.hideDescription = true;
		
		$scope.viewDescription = function(index) {
			$scope.selectedItem = $scope.items[index];
			$scope.hideDescription = false;
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
