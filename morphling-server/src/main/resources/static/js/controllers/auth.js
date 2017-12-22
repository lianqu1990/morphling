'use strict';

/* Controllers */
// signin controller
app.controller('AuthController', ['$rootScope', '$scope', '$state', '$cookieStore','CardAccountService','dbUtils',function ($rootScope, $scope, $state,$cookieStore,cardAccountService,dbUtils) {
    $scope.showWarning = true;
    $scope.closeWarning = function(){
        $scope.showWarning = null;
    }

    $scope.submit = function(){
        if($scope.form.$invalid){return};
        cardAccountService.auth({
            phone:$scope.phone,
            realname:$scope.realname
        }).then(function(response){
            if (response.code == SUCCESS_CODE) {
                dbUtils.success("授权成功，用户\""+$scope.realname+"\"已经成为您的下线代理！","提示");
            } else {
                dbUtils.error(response.message,"提示");
            }
        });
    }
}])
;