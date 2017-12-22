
'use strict';

var fooTableDirectives = angular.module('ui.components.footable', ['dbUtils']);
fooTableDirectives.directive('uiFooTable', ['dbUtils', 'dateFilter', '$compile','$sce',function (dbUtils, dateFilter,$compile,$sce) {
    return {
        restrict: 'E',
        templateUrl: "tpl/templates/uiFooTable.html",
        replace: true,
        transclude:true,
        controller: ['$scope', function ($scope) {
            //var _tobecompile = [];
            //queryForm
            if (angular.isUndefined($scope.table)) {
                return;
            }

            if(angular.isUndefined($scope.table.autoLoad) || $scope.table.autoLoad){
                queryData();
            }



            function queryData(){
                $scope.table.rows = [];
                $scope.table.beforeReload && $scope.table.beforeReload();
                $scope.loading = true;
                var queryParams = $scope.table.params || {};
                dbUtils.get($scope.table.url,queryParams).then(function(response){
                    //_tobecompile = [];
                    if(response.code != SUCCESS_CODE){
                        dbUtils.warning(response.message,"提示");
                        $scope.loading = false;
                        return;
                    }
                    var data = response.data;//这个必须是数组数据
                    $scope.table._rows = data;

                    for(var i in $scope.table._rows){
                        var _row = $scope.table._rows[i];
                        var row = {};
                        for(var j in $scope.table.headers){
                            var header = $scope.table.headers[j];
                            var value = _row[header.field] + '';
                            value = value || '';
                            if(header.formatter){
                                value = header.formatter(value,_row);
                            }
                            if(header.compile && !header.directive){
                                value = $sce.trustAsHtml(value);
                            }
                            // if(header.compile && header.directive){
                            //     _tobecompile.push({
                            //         "id":"#__"+(header.field ? header.field : "")+i,
                            //         "value":value
                            //     });
                            // }
                            row[header.field] = value;
                        }
                        $scope.table.rows.push(row);
                    }
                    $scope.loading = false;




                    $scope.$on("fooTableLoadOver",function(){
                        // for(var i in _tobecompile){
                        //     var element = $compile(_tobecompile[i]["value"])($scope);
                        //     angular.element(_tobecompile[i]["id"]).replaceWith(element);
                        // }
                    })
                });
            }

            $scope.table.operations = {
                reloadData: function(){
                    queryData();
                },
                //每行点击事件
                checkedRow: function(row){
                    row.checked = !row.checked;
                    checkAllowSelect();
                },
                //点击全选复选框事件
                allRowChecked: function(){
                    $scope.table.settings.allRowChecked = !$scope.table.settings.allRowChecked;
                    angular.forEach($scope.table.rows, function (row) {
                        row.checked = $scope.table.settings.allRowChecked;
                    });
                },
                //grid上方按钮点击事件
                operationButtonClick: function(clickFun){
                    var rows = getAllSelectRows();
                    clickFun(rows);
                    checkAllowSelect();
                }

            };
            function checkAllowSelect() {
                if (!$scope.table.settings.showCheckBox) {
                    return;
                }
                //如果所有行数据为非选中状态，则全选按钮为非选中状态，反之一样
                var flag = true;
                if (!$scope.table.rows || $scope.table.rows.length == 0) {
                    flag = false;
                }
                angular.forEach($scope.table.rows, function (row) {
                    if (!row.checked) {
                        flag = false;
                    }
                });
                $scope.table.settings.allRowChecked = flag;
            }

            //获取所有选中的行数据
            function getAllSelectRows() {
                var rows = [];
                angular.forEach($scope.table.rows, function (row,i) {
                    if (row.checked) {
                        rows.push($scope.table._rows[i]);
                    }
                });
                return rows;
            }
            //事件处理
            $scope.publishEvent = function(){
                console.log('publish event...');
                var args = [];
                for(var i = 0;i<arguments.length;i++){
                    args.push(arguments[i]);
                }
                $scope.$emit.apply(this,args);
            }
        }],
        link: function (scope, element, attrs, controller) {

        }
    }

}]);
fooTableDirectives.directive('listenFooTableFinish', ['$timeout', function ($timeout) {
    return {
        restrict:"A",
        link: function(scope,element,attr) {
            if (scope.$last === true) {
                $timeout(function(){
                    scope.$emit("fooTableLoadOver")
                });
            }
        }
    };
}])