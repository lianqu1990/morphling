'use strict';

app.controller('ClientController', ['$scope','$rootScope','$modal','$compile','EchoLog','dbUtils','ClientService',function($scope,$rootScope,$modal,$compile,echoLog,dbUtils,clientService) {
    $scope.errorNum = 0;
    var table = {
        url:"/client",
        headers: [
            {name:"名称",field:"name"},
            {name:"地址",field:"hostAddress"},
            {name:"端口",field:"port"},
            {name:"备注",field:"remark"},
            {name:"创建时间",field:"createTime"},
            {name:"状态",class:"text-center",compile:true,formatter:function(value,row){
                clientService.health({
                    "host":row.hostAddress,
                    "port":row.port
                }).then(function (response){
                    if(response.code == SUCCESS_CODE){
                        //请求成功，状态判断
                        $scope.health[row.id] = response.data.status;
                        $scope.healthDetail[row.id] = dbUtils.toString(response.data);
                    }else{
                        $scope.errorNum+=1;
                        $scope.health[row.id] = "ERROR";
                        $scope.healthDetail[row.id] = response.message;
                    }
                })
                return "<span ng-if='!health["+row.id+"]'><i class='fa fa-spin fa-spinner '></i></span>" +
                    "<span class='label bg-success cusor-pointer'  endpoint-show =\"{{healthDetail["+row.id+"]}}\"  ng-if='health["+row.id+"] == \"UP\"'>UP</span>"+
                    "<span class='label bg-danger cusor-pointer'  endpoint-show=\"{{healthDetail["+row.id+"]}}\"  ng-if='health["+row.id+"] == \"DOWN\"'>DOWN</span>"+
                    "<span class='label bg-dark ' tooltip-placement=\"left\" tooltip=\"{{healthDetail["+row.id+"]}}\"  ng-if='health["+row.id+"] == \"ERROR\"'>ERROR</span>";
            }}
        ],
        rowEvents:[{
            class:"btn-danger",
            name:"更新",
            isShow:function(row){
                return row.packVersion == "1";
            },
            click: function(row){
                var instance = $modal.open({
                    animation: true,
                    templateUrl: 'tpl/system/client_remove.html',
                    controller: 'ClientModifyController',
                    size: "lg",
                    backdrop: "static",
                    resolve: {
                        source: function () {
                            return {"data":row,"action":"update"};
                        }
                    }
                });
                instance.result.then(function (id) {
                    //开启日志查看窗口
                    echoLog.show(id,function(){
                        $scope.table.operations.reloadData();
                    });
                });
            }
        },{
            class:"btn-dark",
            name:"删除",
            click: function(row){
                var instance = $modal.open({
                    animation: true,
                    templateUrl: 'tpl/system/client_remove.html',
                    controller: 'ClientModifyController',
                    size: "lg",
                    backdrop: "static",
                    resolve: {
                        source: function () {
                            return {"data":row,"action":"delete"};
                        }
                    }
                });
                instance.result.then(function (id) {
                    //开启日志查看窗口
                    echoLog.show(id,function(){
                        $scope.table.operations.reloadData();
                    });
                });
            }
        }],
        beforeReload:function(){
            $scope.health = {};
            $scope.healthDetail = {};
        }
    }
    $scope.addClient = function(){
        var instance = $modal.open({
            animation: true,
            templateUrl: 'tpl/system/client_add.html',
            controller: 'ClientAddController',
            size: "lg",
            backdrop: "static",
            resolve: {
                source: function () {
                    return {};
                }
            }
        });
        instance.result.then(function (id) {
            //开启日志查看窗口
            echoLog.show(id,function(){
                $scope.table.operations.reloadData();
            });
        });
    }
    $scope.table = table;
}]);



app.controller('ClientAddController', ['dbUtils','$scope','$modalInstance', '$state','source','ClientService', function (dbUtils,$scope,$modalInstance, $state,source,clientService) {

    $scope.client = {};
    $scope.client.port = 21111;
    $scope.client.username = "deploy";
    $scope.client.serverPort = 22;

    $scope.cancel = function () {
        $modalInstance.dismiss('cancel');
    };

    $scope.submitApply = function(){
        //新增
        if($scope.form.$invalid){
            return;
        }
        clientService.save($scope.client).then(function(response){
            if (response.code == SUCCESS_CODE) {
                var id = response.data;
                dbUtils.success("创建成功","提示");
                $modalInstance.close(id);
            } else {
                dbUtils.error(response.message,"提示");
            }

        });
    }
}]);

app.controller('ClientModifyController', ['dbUtils','$scope','$modalInstance', '$state','source','ClientService', function (dbUtils,$scope,$modalInstance, $state,source,clientService) {

    $scope.client = source.data;
    $scope.client.username = "deploy";
    $scope.client.serverPort = 22;

    $scope.cancel = function () {
        $modalInstance.dismiss('cancel');
    };

    $scope.submitApply = function(){
        if($scope.form.$invalid){
            return;
        }
        if(source.action == "delete"){
            clientService.remove($scope.client).then(function(response){
                if (response.code == SUCCESS_CODE) {
                    var id = response.data;
                    dbUtils.info("删除成功","提示");
                    $modalInstance.close(id);
                } else {
                    dbUtils.error(response.message,"提示");
                }

            });
        }else if(source.action == "update") {
            clientService.update($scope.client).then(function (response) {
                if (response.code == SUCCESS_CODE) {
                    var id = response.data;
                    dbUtils.info("更新成功","提示");
                    $modalInstance.close(id);
                } else {
                    dbUtils.error(response.message,"提示");
                }
            })
        }


    }
}]);