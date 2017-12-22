'use strict';

angular.module("app").service('UserService', ['dbUtils', '$http', '$q', function (dbUtils, $http, $q) {
    return {
        getInfo:function(){
            return dbUtils.get("/userinfo")
        },
        setInfo:function(data){
            return dbUtils.post("/userinfo",data);
        },
        add:function(user){
            return dbUtils.post("/user",user);
        },
        delete:function(id){
            return dbUtils.delete("/user/"+id);
        },
        listApps:function(id){
            return dbUtils.get("/user/"+id+"/apps");
        },
        addApp:function(userid,appIds){
            return dbUtils.post("/user/"+userid+"/apps",{appIds:appIds});
        },
        deleteApp:function(userid,id){
            return dbUtils.delete("/user/"+userid+"/apps/"+id);
        }
    }
}]);