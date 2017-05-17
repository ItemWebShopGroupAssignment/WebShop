/**
 * Admin Controller.
 */

(function() {
	
	var app = angular.module("application");
	
	var adminController = function($scope, $http) {
		$scope.title = "Items";
		$scope.editedContents = "editedContents";
		$scope.editedCategoryName = "editedCategoryName";
		
		var categories = [];
		$scope.categories = categories;
		
		var items = [];
		$scope.items = items;
		
		$scope.inStock = "Hm";
		$scope.value = "Action";
		
		$scope.showGames = true;
		$scope.showOrders = false;
		$scope.showAddPage = false;
		$scope.showEditPage = true;
		$scope.showAddCategoryPage = false;
		$scope.showEditCategoryPage = false;
		$scope.showOrderedItems = false;
		$scope.addOrEditGame = "Add game";
		$scope.addOrEditCategory = "Add category";
		$scope.gamesOrOrders = "Show orders";
		
		$scope.showOrders = function() {
			if ($scope.showOrders == false) {
				$scope.showOrders = true;
				$scope.showGames = false;
			}
			else if ($scope.showGames == false) {
				$scope.showGames = true;
				$scope.showOrders = false;
			}
		}
		
		$scope.showAddForm = function() {
			if ($scope.showAddPage == false) {
				$scope.showAddPage = true;
				$scope.showEditPage = false;
				$scope.showAddCategoryPage = false;
				$scope.showEditCategoryPage = false;
				$scope.showOrderedItems = false;
				$scope.addOrEditGame = "Edit game";
			}
			else if ($scope.showEditPage == false){
				$scope.showEditPage = true;
				$scope.showAddPage = false;
				$scope.showAddCategoryPage = false;
				$scope.showEditCategoryPage = false;
				$scope.showOrderedItems = false;
				$scope.addOrEditGame = "Add game";
			}
		}
		
		$scope.showCategoryForm = function() {
			if ($scope.showAddCategoryPage == false) {
				$scope.showAddCategoryPage = true;
				$scope.showAddPage = false;
				$scope.showEditPage = false;
				$scope.showEditCategoryPage = false;
				$scope.showOrderedItems = false;
				$scope.addOrEditCategory = "Edit category";
			}
			else if ($scope.showEditCategoryPage == false) {
				$scope.showEditCategoryPage = true;
				$scope.showAddCategoryPage = false;
				$scope.showAddPage = false;
				$scope.showEditPage = false;
				$scope.showOrderedItems = false;
				$scope.addOrEditCategory = "Add category";
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
		
		var onAddCategoryComplete = function(response) {
			$http.get("GetItems").then(onItemsComplete, onError);
		}
		
		var onEditCategoryComplete = function(response) {
			$http.get("GetItems").then(onItemsComplete, onError);
		}

		var onError = function(reason) {
			$scope.error = "Could not fetch data " + reason.status;
		}
		
		$scope.editCategory = function(category, contents, oldCategoryName) {
			var parameters = {
					'categoryName' : category,
					'contents' : contents,
					'oldCategoryName' : oldCategoryName
			}
			
			var jsonPackage = JSON.stringify(parameters);
			
			$http.post("EditCategory", jsonPackage).then(onEditCategoryComplete, onError);
		}
		
	
		$scope.addCategory = function(category, contents) {
			var parameters = {
					'categoryName' : category,
					'contents' : contents
			}
			
			var jsonPackage = JSON.stringify(parameters);
			
			$http.post("AddCategory", jsonPackage).then(onAddCategoryComplete, onError);
		}
		
		$scope.addToInventory = function(artNr, count) {
			var parameters = {
					'artNr' : artNr,
					'stockBalance' : count
			};
			
			var jsonPackage = JSON.stringify(parameters);
			
			$http.post("IncreaseStockBalance", jsonPackage).then(onIncreaseStockBalanceComplete, onError);
		}
		
		$scope.removeFromInventory = function(artNr, count, stockBalance) {
			if (count > stockBalance) {
				count = stockBalance;
			}
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

		$scope.viewDescription = function(id) {
			for(var i = 0; i < $scope.items.length; i++) {
				if(id === $scope.items[i].artNr) {
					$scope.selectedItem = $scope.items[i];
					$scope.hideDescription = false;
					break;
				}
			}

		}
		
		$scope.selectCategory = function(id) {
			for(var i = 0; i < $scope.categories.length; i++) {
				if(id.categoryName === $scope.categories[i].categoryName) {
					$scope.selectedCategory = $scope.categories[i];
					$scope.oldCategoryName = id.categoryName;
					$scope.hideDescription = false;
					break;
				}
			}
		}

	};
	
	app.controller("adminController", [ "$scope", "$http", adminController ]);
	
}())