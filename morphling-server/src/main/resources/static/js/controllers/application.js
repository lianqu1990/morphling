'use strict';

app.controller('ApplicationController', ['$scope','$rootScope','$modal','$state','dbUtils','ApplicationService',function($scope,$rootScope,$modal,$state,dbUtils,appService) {
    var table = {
        url:"/app",
        params:[
            {name:"name",label:"应用名",labelCols:4,type:"text"}
        ],
        headers: [
            {name:"名称",field:"name"},
            {name:"描述",field:"description"},
            {name:"上下文路径",field:"contextPath"},
            {name:"端口",field:"port"},
            {name:"创建时间",field:"createTime"}
        ],
        operationEvents: [{
            class:"btn-primary",
            icon:"glyphicon glyphicon-plus",
            name:"创建应用",
            click:function(){
                var instance = $modal.open({
                    animation: true,
                    templateUrl: 'tpl/app/app_edit.html',
                    controller: 'ApplicationEditController',
                    size: "lg",
                    backdrop: "static",
                    resolve: {
                        source: function () {
                            return {}
                        }
                    }
                });
                instance.result.then(function () {
                    $scope.table.operations.reloadData()
                });
            }
        }],
        rowEvents:[{
            class:"btn-success",
            name:"实例管理",
            click: function(row){
                //
                console.log($state.go)
                $state.go("system.instance", {appId: row.id})
            }
        },{
            class:"btn-primary",
            name:"编辑",
            click: function(row){
                //
                var instance = $modal.open({
                    animation: true,
                    templateUrl: 'tpl/app/app_edit.html',
                    controller: 'ApplicationEditController',
                    size: "lg",
                    backdrop: "static",
                    resolve: {
                        source: function () {
                            return row;
                        }
                    }
                });
                instance.result.then(function () {
                    $scope.table.operations.reloadData()
                });
            }
        },{
            class:"btn-danger",
            name:"删除",
            isShow:function(row){
                return row.username != "admin";
            },
            click:function(row){
                dbUtils.error("暂不支持！","提示");
            }
        }],
        settings:{
            cols:3,
            showCheckBox:false
        }
    }
    $scope.table = table;
}]);



app.controller('ApplicationEditController', ['$scope','$rootScope','$modalInstance','$compile','dbUtils','source','ApplicationService',function($scope,$rootScope,$modalInstance,$compile,dbUtils,source,appService) {

    appService.getEnvs().then(function(response){
        $scope.envs = response.data;
    });

    if(angular.isUndefined(source.id)){
        $scope.editData = false;
    }else{
        $scope.editData = true;
        delete source.createTime;delete source.properties;
        $scope.app = source;
    }

    $scope.cancel = function () {
        $modalInstance.dismiss('cancel');
    };

    $scope.submitApply = function(){

        if($scope.form.$invalid){
            return;
        }
        var envs = [];
        angular.forEach(angular.element("input[name=envs]"),function(e,key){
            if(e.checked){
                envs.push(e.value)
            }
        });
        if(envs.length == 0){
            dbUtils.error("请至少选择一个环境","提示");
        }
        appService.saveOrUpdate($scope.app,envs.join()).then(function(response){
            if (response.code == SUCCESS_CODE) {
                dbUtils.info("添加成功","提示");
                $modalInstance.close();
            } else {
                dbUtils.error(response.message,"提示");
            }
        })
    }
}]);



app.controller('InstanceAdminController', ['$scope','$rootScope','$compile','dbUtils','$stateParams','EchoLog','ApplicationService',function($scope,$rootScope,$compile,dbUtils,$stateParams,echoLog,appService) {
    $scope.appId = $stateParams.appId;
    function loadInstances(){
        appService.getInstances($scope.appId).then(function(response){
            $scope.instances = response.data;

        })
    }

    loadInstances();

    var table = {
        url:"/app/"+$scope.appId+"/clients",
        headers: [
            {name:"名称",field:"name"},
            {name:"地址",field:"hostAddress"},
            {name:"端口",field:"port"},
            {name:"备注",field:"remark"},
            {name:"创建时间",field:"createTime"}
        ],
        rowEvents:[{
            class:"btn-dark",
            name:"添加实例",
            click: function(row){
                appService.addInstance($scope.appId,row.id).then(function(){
                    loadInstances();
                    $scope.table.operations.reloadData();
                })
            }
        }],
        beforeReload:function(){

        }
    }

    $scope.table  = table;

    $scope.deleteInstance = function(insId,clientName){
        dbUtils.confirm("确定要删除【"+clientName+"】上的实例吗？",function(){
            appService.deleteInstance($scope.appId,insId).then(function(response){
                if(angular.isNumber(response.data)){
                    //需要查看日志
                    echoLog.show(response.data,function(){

                    });
                }
                loadInstances();
                $scope.table.operations.reloadData();
            });
        })
    }

}]);

