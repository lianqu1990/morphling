'use strict';
//实例太多的时候可以使用分页table,可以防止请求量太大的问题
app.controller('DegradeManageController', ['$scope','$rootScope','$modal','$state','$stateParams','dbUtils','EchoLog','ApplicationService','EndpointService','DegradeManageService',function($scope,$rootScope,$modal,$state,$stateParams,dbUtils,echoLog,appService,endpointService,degradeService) {
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

    $scope.closeWarning = function(){
        $scope.warningShow =false;
    }


    function showInfo(){
        degradeService.show($scope.appId).then(function(response){
            $scope.degradeEndpoints = response.data;
            $scope._degradeInfo = {};
            angular.forEach(response.data,function(e){
                $scope._degradeInfo[e.key] = e.open;
            })
        })
    }

    showInfo();

    $scope.submit = function(){
        dbUtils.confirm("确定要继续操作吗？",function(){
            var data = {};
            var changed = false;
            var elements = angular.element("table[name='degradeForm']").find("input[type='checkbox']");
            elements.each(function(i,e){
                data[e.name] = e.checked;//放在这里，是为了把release版本的给后台，保证所见即所得
                if($scope._degradeInfo[e.name] != e.checked){
                    changed = true;
                }
            })
            if(changed){
                $scope.loading = true;
                degradeService.degrade($scope.appId,data).then(function(response){
                    if(response.code == SUCCESS_CODE){
                        dbUtils.success("操作成功！","提示");
                        showInfo();
                    }else{
                        dbUtils.error(response.message,"提示");
                    }
                    $scope.loading = false;
                })
            }else{
                dbUtils.info("未做任何修改");
            }
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