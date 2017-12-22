'use strict';

/* Controllers */
// signin controller
app.controller('SigninFormController', ['$rootScope', '$scope', '$state', '$cookieStore','AccountService','$modal','dbUtils', function ($rootScope, $scope, $state,$cookieStore, accountService,$modal,dbUtils) {
    //默认自动登录为true
    $scope.user={rememberMe:true};
    $scope.codeurl = accountService.getValidImgCode();
    $scope.login = function () {
        $scope.errInfo = null;
        $scope.errCode = null;
            accountService.login($scope.user).then(function (response) {
            if (response.code == SUCCESS_CODE) {
                $rootScope._userinfo = response.data;
                $rootScope._userinfo.currentEnv = accountService.getEnv($rootScope._userinfo.envs);
                dbUtils.success("登录成功","提示");
                $state.go("app.home");
            } else {
                console.log(response);
                $scope.errInfo = response.message;
                $scope.errCode = response.code;
                if(response.code != 1101){
                	$scope.codeurl = accountService.getValidImgCode();//刷新验证码
                }
            }
        });
    };
    $scope.refreshCaptcha = function () {
        $scope.codeurl = accountService.getValidImgCode();
    }

}]);

