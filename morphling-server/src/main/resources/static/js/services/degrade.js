'use strict';

angular.module("app").service('DegradeManageService', ['dbUtils', '$http', '$q', function (dbUtils, $http, $q) {
    return {
        show:function(appId){
            return dbUtils.get("/degrade/"+appId);
        },
        degrade:function(appId,data){
            return dbUtils.postBody("/degrade/"+appId,data)
        }
    }
}]);