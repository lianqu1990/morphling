'use strict';

app.controller('SelectAppController', ['$scope','$rootScope','$modal','$state','$stateParams','dbUtils','ApplicationService',function($scope,$rootScope,$modal,$state,$stateParams,dbUtils,appService) {
    appService.userPreview().then(function(response){
        $scope.apps = response.data;
    })

    $scope.random = function(){
        return parseInt(5*Math.random())
    }

    $scope.selectApp = function(appId){
        $state.go($stateParams.from,{"appId":appId})
    }
}]);

