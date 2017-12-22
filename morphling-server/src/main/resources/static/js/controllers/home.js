'use strict';

/* Controllers */
// signin controller
app.controller('HomeController', ['$rootScope', '$scope', '$state', '$cookieStore','BaseInfoService',function ($rootScope, $scope, $state,$cookieStore,baseInfoService) {
    $scope.showWarning = true;
    $scope.closeWarning= function(){
        $scope.showWarning = null;
    }
}])
;