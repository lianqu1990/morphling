'use strict';
//实例太多的时候可以使用分页table,可以防止请求量太大的问题
app.controller('DeployController', ['$scope','$rootScope','$modal','$state','$stateParams','dbUtils','EchoLog','ApplicationService','EndpointService','DeployService',function($scope,$rootScope,$modal,$state,$stateParams,dbUtils,echoLog,appService,endpointService,deployService) {
    if(angular.isUndefined($stateParams.appId) || isNaN(parseInt($stateParams.appId))){
        $state.go("app.selectApp",{from:$state.current.name})
        return;
    }

    $scope.appId = $stateParams.appId;

    function beforeLoad(){
        $scope.monitor = {"status":{},'registStatus':{},"nginxStatus":{},"healthStatus":{},"healthDetail":{},"info":{}};
        $scope.stoppedCount = 0;
        $scope.offlineCount = 0;
        $scope.illedCount = 0;
    }

    beforeLoad();


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
                $scope.monitor['status'][row.id] = value;//首次初始化该状态
                if(value == 2){
                    $scope.stoppedCount+=1;
                }
                return "<span class='badge ' ng-class='{0:\"bg-dark\",1:\"bg-success\",2:\"bg-danger\"}[monitor.status["+row.id+"]]'>{{{0:\"待部署\",1:\"运行中\",2:\"已停止\"}[monitor.status["+row.id+"]]}}</span>"
            }},
            {name:"注册中心状态",field:"registStatus",compile:true,formatter:function(value,row){
                $scope.monitor['registStatus'][row.id] = row.registStatus;//首次初始化该状态
                if(!row.registStatus){
                    $scope.offlineCount+=1;
                }
                return "<span class='badge ' ng-class='{true:\"bg-success\",false:\"bg-danger\"}[monitor.registStatus["+row.id+"]]'>{{{true:\"在线\",false:\"离线\"}[monitor.registStatus["+row.id+"]]}}</span>"
            }},
            {name:"健康检测",class:"text-center",compile:true,field:"_health",formatter:function(value,row){
                if(row.status == 1){
                    endpointService.check(row.host,row.port,row.contextPath,"/health").then(function(response){
                        if(response.code== SUCCESS_CODE){
                            $scope.monitor['healthStatus'][row.id] = response.data.status;
                            $scope.monitor['healthDetail'][row.id] =dbUtils.toString(response.data);
                        }else{
                            var obj = {"status":"ERROR","message":"连接失败"};
                            $scope.monitor['healthStatus'][row.id] = obj.status;
                            $scope.monitor['healthDetail'][row.id] =dbUtils.toString(obj);
                        }
                    });
                }else{
                    return "<span class='badge bg-dark'>未运行</span> ";
                }
                // if($scope.app.serviceType == 1){
                //     endpointService.check(row.host,$scope.app.port,$scope.app.contextPath,"/webRegister").then(function(response){
                //         $scope.monitor['nginxStatus'][row.id] = response.data;
                //     });
                // }
                return "<span ng-if='!monitor.healthStatus["+row.id+"]'><i class='fa fa-spin fa-spinner '></i></span>" +
                    "<span ng-if='monitor.healthStatus["+row.id+"]' class='label cusor-pointer' ng-class=\"{'UP':'bg-success','DOWN':'bg-error','ERROR':'bg-dark'}[monitor.healthStatus["+row.id+"]]\"   endpoint-show =\"{{monitor.healthDetail["+row.id+"]}}\"  >{{{'UP':'正常','DOWN':'异常','ERROR':'连接失败'}[monitor.healthStatus["+row.id+"]]}}</span>";
            }},
            {name:"基本信息",class:"text-center",compile:true,formatter:function(value,row){
                if(row.status == 1){
                    endpointService.check(row.host,row.port,row.contextPath,"/info").then(function(response){
                        if(response.code== SUCCESS_CODE){
                            $scope.monitor['info'][row.id] =dbUtils.toString(response.data);
                        }else{
                            $scope.monitor['info'][row.id] = "{\"status\":\"ERROR\",\"message\":\"连接失败\"}";
                        }
                    });
                }else{
                    return "<span class='badge bg-dark'>未运行</span> ";
                }

                return "<span ng-if='!monitor.info["+row.id+"]'><i class='fa fa-spin fa-spinner '></i></span>" +
                    "<span ng-if='monitor.info["+row.id+"]' class='label cusor-pointer bg-primary'   endpoint-show =\"{{monitor.info["+row.id+"]}}\"  >查看</span>";
            }}
        ],
        rowEvents:[{
            class:"btn-warning",
            name:"NGX下线",
            isShow:function(row){
                return $scope.app.serviceType == 1 && row.registStatus;
            },
            click: function(row){
                dbUtils.confirm("确定要从nginx下线吗？",function(){
                    $scope.loading = true;
                    deployService.unRegist(row.id,$scope.app.serviceType).then(function(response){
                        $scope.loading = false;
                        if(response.data){
                            //true则执行成功
                            row.registStatus = false;
                            $scope.monitor['registStatus'][row.id] = false;
                            $scope.offlineCount +=1;
                        }else{
                            dbUtils.error("执行失败，请刷新数据确认！");
                        }
                    })
                })
            }
        },{
            class:"btn-success",
            name:"NGX上线",
            isShow:function(row){
                return row.status == 1 && $scope.app.serviceType == 1 && !row.registStatus;
            },
            click: function(row){
                dbUtils.confirm("确定要上线到nginx吗?",function(){
                    $scope.loading = true;
                    deployService.regist(row.id,$scope.app.serviceType).then(function(response){
                        $scope.loading = false;
                        if(response.data){
                            //true则执行成功
                            row.registStatus = true;
                            $scope.monitor['registStatus'][row.id] = true;
                            $scope.offlineCount -=1;
                        }else{
                            dbUtils.error("执行失败，请刷新数据确认！");
                        }
                    })
                })

            }
        },{
            class:"btn-info",
            name:"部署",
            click:function(row){
                deploy("deploy",[row.id])
            }
        },{
            class:'btn-info',
            name:"重启",
            isShow:function(row){
                return row.status != 0;
            },
            click:function(row){
                deploy("restart",[row.id])
            }
        },{
            class:'btn-info',
            name:"停止",
            isShow:function(row){
               return row.status == 1;
            },
            click:function(row){
                deploy("stop",[row.id])
            }
        },{
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
        beforeReload:beforeLoad,
        settings:{
            pagerHide:false,
            showCheckBox:true
        }
    }

    function deploy(action,instances){
        if(instances && instances.length != 0){
            dbUtils.confirm("确定继续操作？",function(){
                deployService.deploy({
                    action:action,
                    instanceIds:instances.join(),
                    appId:$scope.appId,
                    parallel:$scope.parallel
                }).then(function(response){
                    if(response.code == SUCCESS_CODE){
                        echoLog.show(response.data,function(){
                            $scope.table.operations.reloadData();
                        });
                    }else{
                        dbUtils.error(response.message,"提示");
                    }

                })
            });
        }

    }

    $scope.table = table;








    // ----------------------------部署日志 start---------------------------------------------

    $scope.logs = [];
    $scope.logCursor = 0;
    $scope.logEnd = false;

    $scope.loadLogs = function (){
        deployService.deployLog($scope.appId,$scope.logCursor).then(function(response){
            if(response.code == SUCCESS_CODE){
                if(response.data.length > 0){
                    $scope.logs = $scope.logs.concat(response.data)
                    $scope.logCursor+=10;
                }
                if(response.data.length < 10){
                    $scope.logEnd = true;
                }
            }
        })
    }
    $scope.loadLogs();



    // -----------------------------部署日志 end------------------------------------------


}]);

app.controller('SelectTagController', ['$scope','$rootScope','$modalInstance','$compile','dbUtils','source','DeployService',function($scope,$rootScope,$modalInstance,$compile,dbUtils,source,deployService) {

    $scope.cancel = function () {
        $modalInstance.dismiss('cancel');
    };

    var table = {
        url:"/deploy/"+source.id+"/tags",
        headers: [
            {name:"名称",field:"tagName",compile:true,formatter:function(value,row){
                return "<span class='label bg-success text-md'>"+value+"</span>"
            }},
            {name:"备注",field:"remark"},
            {name:"创建时间",field:"createTime"}
        ],
        rowEvents:[{
            class:"btn-primary",
            name:"打包",
            click: function(row){
                $modalInstance.close(row);
            }
        }],
        beforeReload:function(){

        },
        settings:{
            filterId:"subSelectTagFilter"
        }
    }

    $scope.table  = table;

}]);
