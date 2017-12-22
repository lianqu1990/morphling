'use strict';

angular.module("app").service('EndpointService', ['dbUtils', '$http', '$q', function (dbUtils, $http, $q) {
    return {
        check:function(host,port,context,endpoint){
            return dbUtils.get("/endpoint",{
                "host":host,
                "port":port,
                "context":context,
                "endpoint":endpoint
            })
        }
    }
}]);