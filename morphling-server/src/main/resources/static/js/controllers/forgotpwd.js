'use strict';

/* Controllers */
// signin controller
app.controller('ForgotPwdController', ['$scope', '$state','dbUtils', '$modal','$interval','AccountService', function ($scope, $state,dbUtils,$modal,$interval, accountService) {
    $scope.sendtext = "发送验证码";
    $scope.sendCode = function(){
        var instance = $modal.open({
            animation: true,
            templateUrl:'/tpl/validcode/img_validcode.html',
            controller:'ImgValidController',
            size: "sm",
            resolve: {
                source: function () {
                    return {type:2,action:'PhoneValidCode',phone:$scope.phone}
                }
            }
        });
        instance.result.then(function () {
            $scope.sending = true;
            var second = 60, timePromise = undefined;
            timePromise = $interval(function(){
                if(second <= 0){
                    $interval.cancel(timePromise);
                    $scope.sending = undefined;
                    second = 60;
                    $scope.sendtext = "重发验证码";
                }else{
                    $scope.sendtext = second + "秒后可重发";
                    second--;

                }
            },1000,100);
        });
    }
    $scope.doReset = function(){
        accountService.checkCode(2,$scope.phone,$scope.code).then(function(response){
            if(response.code == SUCCESS_CODE){
                $state.go("access.resetpwd", {phone:$scope.phone,code:$scope.code});//跳转到登录界面
            } else {
                dbUtils.error(response.message,"提示");
            }
        });
    }
}])
;