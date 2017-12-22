'use strict';

angular.module("app").service('BaseInfoService', ['dbUtils', '$http', '$q', function (dbUtils, $http, $q) {
    return {
        getProvinces: function(){
            return dbUtils.get("/base/position/provinces");
        },
        getCities: function(provinceId){
            return dbUtils.post("/base/position/cities",{provinceId:provinceId});
        },
        getAreas: function(cityId){
            return dbUtils.post("/base/position/areas",{cityId:cityId});
        },
        agentSaleStatistics: function(){
            return dbUtils.post("/statistics/agentSaleStatistics");
        }
    }
}]);