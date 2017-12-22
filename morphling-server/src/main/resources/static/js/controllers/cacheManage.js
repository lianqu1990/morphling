'use strict';
//实例太多的时候可以使用分页table,可以防止请求量太大的问题
app.controller('CacheManageController', ['$scope','$rootScope','$modal','$state','$stateParams','dbUtils','EchoLog','ApplicationService','EndpointService','CacheManageService',function($scope,$rootScope,$modal,$state,$stateParams,dbUtils,echoLog,appService,endpointService,cacheService) {
    if(angular.isUndefined($stateParams.appId) || isNaN(parseInt($stateParams.appId))){
        $state.go("app.selectApp",{from:$state.current.name})
        return;
    }
    $scope.appId = $stateParams.appId;
    $scope.redis = {};


    function loadApp(){
        appService.appPreview($scope.appId).then(function(response){
            $scope.app = response.data;
            var currentEnv = $rootScope._userinfo.currentEnv;
            if($scope.app.env != currentEnv){
                dbUtils.error("环境错误~")
                $state.go("app.selectApp",{from:$state.current.name})
                return;
            }
        });
    }
    loadApp();


    function loadCache(){
        cacheService.preview($scope.appId).then(function(response){
            $scope.caches = response.data;
        })
    }

    loadCache();

    cacheService.dataSources().then(function(response){
        $scope.dataSources = response.data;
    })


    $scope.editCache = function(ck){
        var instance = $modal.open({
            animation: true,
            templateUrl: 'tpl/cache/cache_edit.html',
            controller: ['$scope','$rootScope','$modalInstance','$compile','dbUtils','source','CacheManageService',CacheEditController],
            size: "lg",
            backdrop: "static",
            resolve: {
                source: function () {
                    return {data:ck,appId:$scope.appId};
                }
            }
        });
        instance.result.then(function (row) {

        });
    }


    $scope.getRedisCache = function(){
        cacheService.getRedis($scope.redis).then(function(response){
            if(!response.data){
                $scope._cacheStr = undefined;
                dbUtils.info("未查询到缓存","提示");
                return;
            }
            try{
                $scope._cacheStr = dbUtils.toString(JSON.parse(response.data));
            }catch(error){
                $scope._cacheStr = response.data;
            }

        })
    }

    $scope.delRedisCache = function(){
        cacheService.delRedis($scope.redis).then(function(response){
            if(response.code == SUCCESS_CODE){
                dbUtils.success("删除成功","提示");
            }

        })
    }


    function CacheEditController($scope,$rootScope,$modalInstance,$compile,dbUtils,source,cacheService){
        $scope.cache = source.data;
        $scope.appId = source.appId;
        console.log($scope.cache)
        $scope.cancel = function () {
            $modalInstance.dismiss('cancel');
        };

        $scope.showCache = function(){
            var params = angular.element("form").serializeArray();
            var data = {};
            for(var i in params){
                if(params[i]['value'] == ""){
                    dbUtils.error("参数有误,"+params[i]['name'])
                    return;
                }
                data[params[i]['name']] = params[i]['value'];
            }
            cacheService.getCache($scope.appId,$scope.cache.id,$scope.cache.sourceType,$scope.cache.cluster,data).then(function(response){
                if(!response.data){
                    $scope._cacheStr = undefined;
                    dbUtils.info("未查询到缓存","提示");
                    return;
                }
                try{
                    $scope._cacheStr = dbUtils.toString(JSON.parse(response.data));
                }catch(error){
                    $scope._cacheStr = response.data;
                }

            })

        }

        $scope.deleteCache = function(){
            var params = angular.element("form").serializeArray();
            var data = {};
            for(var i in params){
                if(params[i]['value'] == ""){
                    dbUtils.error("参数有误,"+params[i]['name'])
                    return;
                }
                data[params[i]['name']] = params[i]['value'];
            }
            cacheService.delCache($scope.appId,$scope.cache.id,$scope.cache.sourceType,$scope.cache.cluster,data).then(function(response){
                if(response.code == SUCCESS_CODE){
                    dbUtils.success("删除成功","提示");
                }
            })
        }
    }


}]);



/*


var table = {
        url:"/app/preview/"+$scope.appId+"/instances",
        params:{checkRegist:false},
        headers: [
            {name:"名称",field:"clientName"},
            {name:"地址",field:"host"},
            {name:"版本",field:"currentVersion"},
            {name:"状态",field:"status",compile:true,formatter:function(value,row){
                return "<span class='badge ' ng-class='{0:\"bg-dark\",1:\"bg-success\",2:\"bg-danger\"}["+row.status+"]'>{{{0:\"待部署\",1:\"运行中\",2:\"已停止\"}["+row.status+"]}}</span>"
            }}
        ],
        rowEvents:[{
            class:'btn-info',
            name:"启动",
            isShow:function(row){
                return row.status == 2;
            },
            click:function(row){
                deploy("start",[row.id])
            }
        }],
        operationEvents:[{
            class:"btn-dark btn-addon",
            icon:"fa fa-globe",
            name:"打包",
            click:function(rows){
                deployService.package($scope.appId,"").then(function(response){
                    if(response.code == 1201){
                        //prod环境，需要选择tag打包
                        var instance = $modal.open({
                            animation: true,
                            templateUrl: 'tpl/deploy/select_tag.html',
                            controller: 'SelectTagController',
                            size: "lg",
                            backdrop: "static",
                            resolve: {
                                source: function () {
                                    return $scope.app;
                                }
                            }
                        });
                        instance.result.then(function (row) {
                            deployService.package($scope.appId,row.tagName).then(function(response){
                                echoLog.show(response.data,function(){
                                    loadApp();
                                });
                            });
                        });
                    }else{
                        echoLog.show(response.data,function(){
                            loadApp();
                        });
                    }
                })
            }
        },{
            class:"btn-primary",
            icon:"fa fa-send",
            name:"部署",
            click:function(rows){
                var ids = [];
                for(var i in rows){
                    ids.push(rows[i].id);
                }
                deploy("deploy",ids);
            }
        },{
            class:"btn-danger",
            icon:"fa fa-repeat",
            name:"重启",
            click:function(rows){
                var ids = [];
                for(var i in rows){
                    ids.push(rows[i].id);
                }
                deploy("restart",ids);
            }
        }],
        settings:{
            pagerHide:false,
            showCheckBox:true
        }
    }

    $scope.table = table;

 */