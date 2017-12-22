'use strict';

angular.module("app").service('RoleService', ['dbUtils', '$http', '$q', function (dbUtils, $http, $q) {
    return {
        list:function(){
            return dbUtils.get("/role")
        },
        listByUser:function(userId){
            return dbUtils.get("/user/"+userId+"/roles");
        },
        setByUser:function(userId,roleIds){
            return dbUtils.post("/user/"+userId+"/roles",{roleId:roleIds})
        }
    }
}]);