'use strict';

/* Controllers */
// signin controller
app.controller('ResetAgentPassController', ['$rootScope', '$scope', '$state', '$cookieStore','CardAccountService','AccountService','dbUtils',function ($rootScope, $scope, $state,$cookieStore,cardAccountService,accountService,dbUtils) {
    $scope.showLoading = false;
    $scope.codeurl = accountService.getValidImgCode();

    $scope.refreshCaptcha = function () {
        $scope.codeurl = accountService.getValidImgCode();
    }

    $scope.reset = function(){
        if($scope.form.$invalid){return};
        $scope.showLoading = true;
        cardAccountService.resetAgentPass({
            id:$scope.id,
            code:$scope.verifyCode
        }).then(function(response){
            if (response.code == SUCCESS_CODE) {
                $scope.newPass = response.data;
                dbUtils.success("重置密码成功！","提示");
            } else {
                dbUtils.error(response.message,"提示");
            }
            $scope.refreshCaptcha();
            $scope.showLoading = false;
        },function(error){
            $scope.refreshCaptcha();
            $scope.showLoading = false;
        });
    }

}])
;