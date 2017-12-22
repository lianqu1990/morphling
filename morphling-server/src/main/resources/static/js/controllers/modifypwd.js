'use strict';

/* Controllers */
// signin controller
app.controller('ModifyPwdController', ['$scope', '$state', '$stateParams', '$cookieStore', 'AccountService','dbUtils', function ($scope, $state, $stateParams, $cookieStore, accountService,dbUtils) {

    $scope.submit = function () {
        if($scope.newPass != $scope.renewPass){
            dbUtils.error("两次密码不一致！","提示");
            return;
        }
        if($scope.newPass == $scope.oldPass){
            dbUtils.error("新密码不能和老密码相同！","提示");
        }
        accountService.modifypwd({
            oldPass:$scope.oldPass,
            newPass:$scope.newPass
        }).then(function(response){
            if (response.code == SUCCESS_CODE) {
                dbUtils.success("修改成功！","提示");
            } else {
                dbUtils.error(response.message,"提示");
            }
        });
    }
}])
;