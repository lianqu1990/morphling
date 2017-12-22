'use strict';

angular.module("app").service('ClientService', ['dbUtils', '$http', '$q','$cookies', function (dbUtils, $http, $q,$cookies) {
    return {
        health: function(data){
            return dbUtils.get("/client/health",data);
        },
        save: function(data){
            return dbUtils.post("/client",data);
        },
        update: function(data){
            return dbUtils.put("/client",data);
        },
        remove: function(data){
            return dbUtils.delete("/client",data);
        }
    }
}]);