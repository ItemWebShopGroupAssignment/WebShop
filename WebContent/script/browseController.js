/**
 *  Browse Controller.
 */

(function() {
	
	// Get the angular module created in routing.js.
	var app = angular.module("application");
	
	var browseController = function($scope, $http, $timeout) {
		
		/*Sort		 */
		$scope.sortOrder = 'category';
		$scope.sortOrder = 'stockBalance';
		$scope.sortOrder = 'price';
		$scope.sortOrder = 'artNr';
		$scope.sortOrder = 'itemName';
		$scope.downArrow = "\u25bc";
		$scope.upArrow = "\u25b2";
		$scope.reverseSort = false;

		
		/* Called when the get items request receives a correct response. */
		var onItemsComplete = function(response) {
			$scope.items = response.data;
		}
		
		/* Called when a HTTP error occurs. */
		var onError = function(reason) {
			alert("Could not fetch data " + reason.status);
		}
		
		/* Get the item list. */
		$http.get("GetItems").then(onItemsComplete, onError);
		
		$scope.title = "";
		$scope.hideDescription = true;

		/* Called the call to AddToCart returns a correct response. */
		var onAddToCartComplete = function(response) {
			// Show a message for 3 seconds. 
			$scope.title = response.data;
			$timeout(function() {
				$scope.title = "";
			}, 3000);
			 
			$http.get("GetItems").then(onItemsComplete, onError);

		}
		
		/* Called when an item in the list is clicked. */
		$scope.viewDescription = function(id) {
			for(var i = 0; i < $scope.items.length; i++) {
				if(id === $scope.items[i].artNr) {
					$scope.selectedItem = $scope.items[i];
					$scope.hideDescription = false;
					break;
				}
			}
		}
		
		/* Called when an item is to be added to the cart. */
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
	
	// Bind the controller.
	app.controller("browseController", [ "$scope", "$http", "$timeout", browseController ]);
	
}())
