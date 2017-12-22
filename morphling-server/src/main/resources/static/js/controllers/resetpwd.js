'use strict';

/* Controllers */
// signin controller
app.controller('ResetPwdController', ['$scope', '$state', '$stateParams', '$cookieStore', 'AccountService','dbUtils', function ($scope, $state, $stateParams, $cookieStore, accountService,dbUtils) {
    $scope.phone = $stateParams.phone;
    $scope.code = $stateParams.code;
    $scope.$watch("newPassword", function () {
        if ($scope.newPassword != $scope.password) {
            $scope.rePasswordInvalid = true;
        } else {
            $scope.rePasswordInvalid = false;
        }
    });

    $scope.resetPass = function () {
        accountService.resetPass({
            phone: $scope.phone,
            code: $scope.code,
            password: $scope.newPassword
        }).then(function (response) {
            if (response.code == SUCCESS_CODE) {
                dbUtils.success("修改成功！","提示");
                $state.go("access.signin");
            } else {
                dbUtils.error(response.message,"提示");
            }
        }, function (fail) {
            //服务器出错
        });
    }
}])
;