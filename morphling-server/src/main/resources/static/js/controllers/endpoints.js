'use strict';
//实例太多的时候可以使用分页table,可以防止请求量太大的问题
app.controller('EndpointsController', ['$scope','$rootScope','$modal','$state','$stateParams','dbUtils','EchoLog','ApplicationService','EndpointService',function($scope,$rootScope,$modal,$state,$stateParams,dbUtils,echoLog,appService,endpointService) {
    if(angular.isUndefined($stateParams.appId) || isNaN(parseInt($stateParams.appId))){
        $state.go("app.selectApp",{from:$state.current.name})
        return;
    }

    $scope.appId = $stateParams.appId;


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


    var table = {
        url:"/app/preview/"+$scope.appId+"/instances",
        params:{checkRegist:true},
        headers: [
            {name:"名称",field:"clientName"},
            {name:"地址",field:"host"},
            {name:"版本",field:"currentVersion"},
            {name:"状态",field:"status",compile:true,formatter:function(value,row){
                if(value == 0){
                    return "<span class='badge bg-dark'>待部署</span>"
                }else if(value == 1){
                    return "<span class='badge bg-success'>运行中</span>"
                }else{
                    return "<span class='badge bg-danger'>已停止</span>"
                }
            }},
        ],
        rowEvents:[{
            class:'btn-primary',
            name:"查看",
            click:function(row){
                $scope.instance = row;
            }
        }],
        operationEvents:[],
        settings:{
            pagerHide:false
        }
    }
    $scope.table = table;


    $scope.simpleShow = function (endpoint){
        $scope.loading = true;
        endpointService.check($scope.instance.host,$scope.instance.port,$scope.instance.contextPath,endpoint).then(function(response){
            $scope.loading = false;
            dbUtils.showJson(dbUtils.toString(response.data))
        },function(error){
            $scope.loading = false;
        })
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