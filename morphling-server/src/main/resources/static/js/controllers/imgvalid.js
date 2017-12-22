
app.controller("ImgValidController",['dbUtils','$scope','$modalInstance', '$rootScope','source','AccountService',function(dbUtils,$scope,$modalInstance, $rootScope,source,accountService){
    $scope.codeurl = accountService.getValidImgCode();
    $scope.ok = function(){
        if(source){
            if(source.action && source.action == 'PhoneValidCode'){
                if(!$scope.verifyCode){
                    dbUtils.warning("请输入验证码!","提示");
                    return;
                }
                accountService.sendCode(source.type,{phone:source.phone,captchar:$scope.verifyCode}).then(function(response){
                    if(response.code == SUCCESS_CODE){
                        dbUtils.success("发送成功","提示");
                        $modalInstance.close('ok');
                    } else {
                        $scope.sending = undefined;
                        dbUtils.error(response.message,"提示");
                        $modalInstance.dismiss('cancel');
                    }
                });
            }
        }else{
            $modalInstance.dismiss('cancel');
        }

    }
    $scope.cancel = function () {
        $modalInstance.dismiss('cancel');
    };
    $scope.refreshCaptcha = function () {
        $scope.codeurl = accountService.getValidImgCode();
    }
}]);