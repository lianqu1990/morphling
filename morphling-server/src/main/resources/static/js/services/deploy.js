'use strict';

angular.module("app").service('DeployService', ['dbUtils', '$http', '$q', function (dbUtils, $http, $q) {
    return {
        regist:function(insId,type){
            return dbUtils.post("/deploy/regist",{id:insId,type:type})
        },
        unRegist:function(insId,type){
            return dbUtils.post("/deploy/unRegist",{id:insId,type:type})
        },
        package:function(appId,tag){
            return dbUtils.get("/deploy/package",{appId:appId,tag:tag})
        },
        deploy:function(data){
            return dbUtils.post("/deploy",data);
        },
        deployLog:function(appId,start){
            return dbUtils.get("/logs/deploy",{appId:appId,start:start})
        }
    }
}]);