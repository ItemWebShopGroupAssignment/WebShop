/**
 * Cart Controller.
 */

(function() {
	
	var app = angular.module("application");
		
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
			 $http.post("DumpCart").then(onDumpedComplete, onCartError);
		}
		
		var onDumpedComplete = function(response) {
			alert(response.data);
			// Do any post-dumping stuff here.
		}
		
		var onCheckoutComplete = function(response) {
			alert(response.data);
		}
		
		$scope.checkoutCart = function() {
			
			$http.post("CheckoutCart").then(onCheckoutComplete, onCartError);
		}
	};
	
	app.controller("cartController", [ "$scope", "$http", cartController ]);
	
}())