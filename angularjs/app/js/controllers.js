var phonecatControllers = angular.module('phonecatControllers', []);

phonecatControllers.controller('PhoneListCtrl', function ($scope) {
  $scope.name = "World"; 	
  $scope.phones = [
    {'name': 'Nexus S',
     'snippet': 'Fast just got faster with Nexus S.',
     'age': 1,
     "imageUrl": "img/phones/phone0.png"
 	},
    {'name': 'Motorola XOOM with Wi-Fi',
     'snippet': 'The Next, Next Generation tablet.',
      'age': 2,
      "imageUrl": "img/phones/phone0.png"
 	},
    {'name': 'MOTOROLA XOOM',
     'snippet': 'The Next, Next Generation tablet.',
      'age': 3,
      "imageUrl": "img/phones/phone0.png"
    }
  ];  
  $scope.orderProp = 'age';
});

phonecatControllers.controller('PhoneDetailCtrl', ['$scope', '$routeParams',
  function($scope, $routeParams) {
    $scope.phoneId = $routeParams.phoneId;
}]);
