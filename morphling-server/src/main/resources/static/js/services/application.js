'use strict';

angular.module("app").service('ApplicationService', ['dbUtils', '$http', '$q', function (dbUtils, $http, $q) {
    return {
        getEnvs:function(){
            return dbUtils.get("/env");
        },
        saveOrUpdate:function(app,envs){
            app.envs = envs;
            return dbUtils.put("/app",app);
        },
        getInstances:function(appId,checkRegist){
            return dbUtils.get("/app/"+appId+"/instances",{"checkRegist":typeof checkRegist === 'undefined' ? false : checkRegist});
        },
        addInstance:function(appId,clientId){
            return dbUtils.post("/app/"+appId+"/instances",{clientId:clientId})
        },
        deleteInstance:function(appId,insId){
            return dbUtils.delete("/app/"+appId+"/instances/"+insId);
        },
        userPreview:function(){
            return dbUtils.get("/app/preview");
        },
        appPreview:function(appId){
            return dbUtils.get("/app/preview/"+appId);
        }
    }
}]);