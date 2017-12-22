'use strict';

/* Controllers */
// signin controller
app.controller('PersonalInfoController', ['$rootScope', '$scope', '$state', 'UserService', 'dbUtils', 'BaseInfoService','$interval', function ($rootScope, $scope, $state, agentService, dbUtils, baseInfoService,$interval) {
    //init
    $scope.info = {};

    // init ok
    agentService.getInfo().then(function (response) {
        if (response.code == SUCCESS_CODE) {
            var data = response.data;
            $scope.info = data;
        }
    });

    $scope.submit = function () {
        if ($scope.form.$invalid) {
            return;
        }
        setInfo();
    }

    function setInfo() {
        agentService.setInfo($scope.info).then(function (response) {
            if (response.code == SUCCESS_CODE) {
                dbUtils.success("修改成功！", "提示");
            } else {
                dbUtils.error(response.message, "提示");
            }
        });
    }

}])
;