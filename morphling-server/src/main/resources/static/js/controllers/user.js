'use strict';

app.controller('UserController', ['$scope','$rootScope','$modal','$compile','dbUtils','UserService',function($scope,$rootScope,$modal,$compile,dbUtils,userService) {
    $scope.errorNum = 0;
    var table = {
        url:"/user",
        params:[
            {name:"name",label:"姓名",labelCols:4,type:"text"},
            {name:"username",label:"用户名",labelCols:4,type:"text"}
        ],
        headers: [
            {name:"姓名",field:"name"},
            {name:"用户名",field:"username"},
            {name:"电话",field:"phone"},
            {name:"邮箱",field:"email"},
            {name:"上次登录时间",field:"lastLoginTime"},
            {name:"上次登录IP",field:"lastLoginIp"},
            {name:"创建时间",field:"createTime"}
        ],
        operationEvents: [{
            class:"btn-info",
            icon:"glyphicon glyphicon-plus",
            name:"添加用户",
            click:function(){
                var instance = $modal.open({
                    animation: true,
                    templateUrl: 'tpl/system/user_add.html',
                    controller: 'UserAddController',
                    size: "lg",
                    backdrop: "static"
                });
                instance.result.then(function () {
                    $scope.table.operations.reloadData()
                });
            }
        }],
        rowEvents:[{
            class:"btn-primary",
            name:"角色",
            isShow:function(row){
                return row.username != "admin";
            },
            click: function(row){
                //
                var instance = $modal.open({
                    animation: true,
                    templateUrl: 'tpl/system/user_role.html',
                    controller: 'UserRoleController',
                    size: "lg",
                    backdrop: "static",
                    resolve: {
                        source: function () {
                            return row;
                        }
                    }
                });
                instance.result.then(function () {

                });
            }
        },{
            class:"btn-info",
            name:"项目",
            isShow:function(row){
                return row.username != "admin";
            },
            click: function(row){
                //
                var instance = $modal.open({
                    animation: true,
                    templateUrl: 'tpl/system/user_app.html',
                    controller: 'UserAppController',
                    size: "lg",
                    backdrop: "static",
                    resolve: {
                        source: function () {
                            return row;
                        }
                    }
                });
                instance.result.then(function () {

                });
            }
        },{
            class:"btn-danger",
            name:"删除",
            isShow:function(row){
                return row.username != "admin";
            },
            click:function(row){
                userService.delete(row.id).then(function(response){
                    if (response.code == SUCCESS_CODE) {
                        dbUtils.info("删除成功","提示");
                        $scope.table.operations.reloadData()
                    } else {
                        dbUtils.error(response.message,"提示");
                    }
                })
            }
        }],
        settings:{
            cols:3,
            showCheckBox:false
        }
    }
    $scope.table = table;
}]);

app.controller('UserAppController', ['$scope','$rootScope','$modalInstance','$compile','dbUtils','source','UserService','RoleService','ApplicationService',function($scope,$rootScope,$modalInstance,$compile,dbUtils,source,userService,roleService,appService) {
    $scope.userId = source.id;
    $scope.username = source.name;
    if(angular.isUndefined($scope.userId)){
        dbUtils.error("数据错误");
        return;
    }

    function loadApps(){
        userService.listApps($scope.userId).then(function(response){
            $scope.apps = response.data;
        })
    }

    loadApps();

    var table = {
        url:"/user/"+$scope.userId+"/apps",
        queryParams:{"exclude":true},
        params:[
            {name:"name",label:"应用名",labelCols:4,type:"text"}
        ],
        headers: [
            {name:"名称",field:"name"},
            {name:"描述",field:"description"},
            {name:"上下文路径",field:"contextPath"},
            {name:"环境",field:"env"},
            {name:"端口",field:"port"},
            {name:"创建时间",field:"createTime"}
        ],
        operationEvents: [{
            class:"btn-primary",
            icon:"fa fa-css3",
            name:"添加所选",
            click:function(rows){
                if(rows.length == 0){
                    return;
                }
                //console
                var appIds = [];
                for(var i in rows){
                    console.log(rows[i])
                    appIds.push(rows[i].id);
                }
                userService.addApp($scope.userId,appIds.join()).then(function(response){
                    loadApps();
                    $scope.table.operations.reloadData();
                });
            }
        }],
        rowEvents:[{
            class:"btn-success",
            name:"添加项目",
            click: function(row){
                //
                console.log(row)
                userService.addApp($scope.userId,row.id).then(function(response){
                    loadApps();
                    $scope.table.operations.reloadData();
                });
            }
        }],
        settings:{
            cols:2,
            showCheckBox:true
        }
    }
    $scope.table = table;

    $scope.cancel = function () {
        $modalInstance.dismiss('cancel');
    };
    $scope.deleteApp = function(id){
        userService.deleteApp($scope.userId,id).then(function(response){
            dbUtils.success("删除成功","提示");
            loadApps();
            $scope.table.operations.reloadData();
        })
    }
}]);

app.controller('UserRoleController', ['$scope','$rootScope','$modalInstance','$compile','dbUtils','source','UserService','RoleService',function($scope,$rootScope,$modalInstance,$compile,dbUtils,source,userService,roleService) {
    roleService.list().then(function (response){
        $scope.roles = response.data;
        roleService.listByUser(source.id).then(function(response){
            for(var i in response.data){
                var r = response.data[i];
                $scope.roleId = r.id;
            }
        });
    })


    $scope.cancel = function () {
        $modalInstance.dismiss('cancel');
    };

    $scope.submitApply = function(){
        if($scope.form.$invalid){
            return;
        }
        roleService.setByUser(source.id,$scope.roleId).then(function(response){
            if (response.code == SUCCESS_CODE) {
                dbUtils.info("配置成功","提示");
                $modalInstance.close();
            } else {
                dbUtils.error(response.message,"提示");
            }
        })
    }
}]);


app.controller('UserAddController', ['$scope','$rootScope','$modalInstance','$compile','dbUtils','UserService',function($scope,$rootScope,$modalInstance,$compile,dbUtils,userService) {

    $scope.cancel = function () {
        $modalInstance.dismiss('cancel');
    };

    $scope.submitApply = function(){
        if($scope.form.$invalid){
            return;
        }
        userService.add($scope.user).then(function(response){
            if (response.code == SUCCESS_CODE) {
                dbUtils.info("添加成功","提示");
                $modalInstance.close();
            } else {
                dbUtils.error(response.message,"提示");
            }
        })
    }
}]);

