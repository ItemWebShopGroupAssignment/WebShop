/**
 * Cart Controller.
 */

(function() {
	
	// Get the angular module created in routing.js.
	var app = angular.module("application");
		
	var cartController = function($scope, $http) {
		$scope.hideDescription = true;
		$scope.amount = 1;
		$scope.info = "Your shopping cart is empty.";
		$scope.showTableHeaders = false;
		$scope.totalCost = 0;
		
		// Call the back-end and ask for the item list for the cart.
		$http.get("GetCartItems").then(onGetCartComplete, onCartError);
		
		/* Called when a list of items is returned. */
		var onGetCartComplete = function(response) {

			$scope.items = response.data;
			
			$scope.totalCost = 0;
			
			if($scope.items.length > 0) {
				$scope.info = "This is the contents of your shopping cart.";
				$scope.showTableHeaders = true;
				
				// Add the shipping fee.
				$scope.totalCost += 29;
			}
		
			for(var i = 0; i < $scope.items.length; i++) {
				// Calculate combined costs for each item.
				$scope.totalCost += ($scope.items[i].price * $scope.items[i].stockBalance);
			}

		}
		
		// Called when a HTTP error has occured.
		var onCartError = function(reason) {
			alert(reason.status);
		}

		/* Called when a remove from cart call responds correctly. */
		var onRemoveComplete = function(response) {
			$http.get("GetCartItems?").then(onGetCartComplete, onCartError);
			
			$scope.hideDescription = true;
			$scope.selectedItem = null;
		}
		
		/* Called every time one of the remove from cart buttons are pressed. */
		$scope.removeFromCart = function(artNr, count){
			var parameters = {
					'artNr' : artNr,
					'stockBalance' : count
			};
			
			var jsonParameters = JSON.stringify(parameters);
			
			$http.post("RemoveFromCart", jsonParameters).then(onRemoveComplete, onCartError);
		}
		
		/* Called when dump cart button is pressed. */
		$scope.dumpCart = function() {
			 $http.post("DumpCart").then(onDumpedComplete, onCartError);
		}
		
		/* Called when the call to DumpCart responds with the correct response. */
		var onDumpedComplete = function(response) {
			/* Update list and info. */
			$scope.items = [];
			$scope.info = "Your shopping cart is empty.";
			$scope.showTableHeaders = false;
			$scope.totalCost = 0;
			
			$scope.hideDescription = true;
			$scope.selectedItem = null;
		}
		
		/* Called when the call to Checkout responds with the correct response. */
		var onCheckoutComplete = function(response) {
			/* Update list and info. */
			$scope.items = [];
			$scope.info = "Your shopping cart is empty.";
			$scope.showTableHeaders = false;
			$scope.totalCost = 0;
			
			$scope.hideDescription = true;
			$scope.selectedItem = null;
		}
		
		/* Called when checkout cart button is pressed. */
		$scope.checkoutCart = function() {
			
			$http.post("CheckoutCart").then(onCheckoutComplete, onCartError);
		}
		
		/* Called when an item in the list is clicked. */
		$scope.showInfo = function(index) {
			$scope.hideDescription = false;
			$scope.selectedItem = $scope.items[index];
		}
	};
	
	// Bind the controller.
	app.controller("cartController", [ "$scope", "$http", cartController ]);
	
}())