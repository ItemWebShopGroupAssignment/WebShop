/**
 * Login Controller.
 */

(function() {
	
	var app = angular.module("application");
	
	var loginController = function($scope, $http) {
		$scope.username = "username";
		$scope.password = "abc123";
		
		var onComplete = function(response) {
			alert("A message has arrived: " + response.data + "!");
		}
		
		var onError = function(reason) {
			alert("A HTTP Error has occured: " + reason.status + "!");
		}
		
		$scope.login = function() {
			var parameters = { 'username' : $scope.username,
					'password' : $scope.password };
			
			$http.post("Login", JSON.stringify(parameters)).then(onComplete, onError);
		}
	};
	
	
	app.controller("loginController", ["$scope", "$http", loginController]);
	
}())