/**
 * Cart Controller.
 */

(function() {
	
	var app = angular.module("application");
		
	var cartController = function($scope, $http) {

		$scope.amount = 1;
		$scope.info = "Your shopping cart is empty.";
		$scope.showTableHeaders = false;
		
		var onGetCartComplete = function(response) {

			$scope.items = response.data;
			
			if($scope.items.length > 0) {
				$scope.info = "This is the contents of your shopping cart.";
				$scope.showTableHeaders = true;
			}
			
			$scope.totalCost = 0;
			for(var i = 0; i < $scope.items.length; i++) {
				$scope.totalCost += ($scope.items[i].price * $scope.items[i].stockBalance);
			}

		}
		
		var onCartError = function(reason) {
			alert(reason.status);
		}

		$http.get("GetCartItems").then(onGetCartComplete, onCartError);

		var onRemoveComplete = function(response) {
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
			 $http.post("DumpCart").then(onDumpedComplete, onCartError);
		}
		
		var onDumpedComplete = function(response) {
			$scope.items = [];
			$scope.info = "Your shopping cart is empty.";
			$scope.showTableHeaders = false;
		}
		
		var onCheckoutComplete = function(response) {
			$scope.items = [];
			$scope.info = "Your shopping cart is empty.";
			$scope.showTableHeaders = false;
		}
		
		$scope.checkoutCart = function() {
			
			$http.post("CheckoutCart").then(onCheckoutComplete, onCartError);
		}
	};
	
	app.controller("cartController", [ "$scope", "$http", cartController ]);
	
}())