
'use strict';
app.filter('propsFilter', function () {
    return function (items, props) {
        var out = [];

        if (angular.isArray(items)) {
            items.forEach(function (item) {
                var itemMatches = false;

                var keys = Object.keys(props);
                for (var i = 0; i < keys.length; i++) {
                    var prop = keys[i];
                    var text = props[prop].toLowerCase();
                    if (item[prop].toString().toLowerCase().indexOf(text) !== -1) {
                        itemMatches = true;
                        break;
                    }
                }

                if (itemMatches) {
                    out.push(item);
                }
            });
        } else {
            // Let the output be the input untouched
            out = items;
        }

        return out;
    };
})
var dbFormGridDirectives = angular.module('ui.components.form.grid', ['dbUtils']);
dbFormGridDirectives.directive('uiFormGrid', ['dbUtils', 'dateFilter', '$sce',function (dbUtils, dateFilter,$sce) {
    return {
        restrict: 'E',
        templateUrl: "tpl/templates/uiFormGrid.html",
        replace: true,
        transclude:true,
        controller: ['$scope', function ($scope) {
            //queryForm
            if (angular.isUndefined($scope.table)) {
                return;
            }

            if(angular.isUndefined($scope.table.queryParams)){
                $scope.table.queryParams = {};
            }
            if(angular.isUndefined($scope.table.settings)){
                $scope.table.settings={};
            }


            //计算每个占用的列数
            var fieldCols = 12;
            if($scope.table.settings && $scope.table.settings.cols){
                fieldCols = 12 / $scope.table.settings.cols;
            }

            var ambiguityFields = {};

            if($scope.table.params){
                angular.forEach($scope.table.params, function (field) {

                    if (!field.options) {
                        field.options = {};
                    }

                    if (angular.isUndefined(field.cols)) {
                        field.cols = fieldCols;
                    }
                    if (angular.isUndefined(field.labelCols)) {
                        field.labelCols = 4;
                    }

                    $scope.table.queryParams[field.name] = "";
                    if (field.type == 'select') {
                        //
                    }
                    if (field.type == '') {
                    }
                    if (field.type == "date") {
                        field['dateFormat'] = field['dateFormat'] || "yyyy-MM-dd";
                        field['dateOptions'] = field['dateOptions'] || {
                                formatYear: 'yy',
                                startingDay: 1,
                                class: 'datepicker',
                                showWeeks: false
                            };
                    }
                    if (field.type == 'dateRange') {
                        field['dateRangeOptions'] = field['dateRangeOptions'] || {
                                autoApply: true,
                                showDropdowns: true,
                                useDefault: false
                            };
                        //固定属性设置
                        field['dateRangeOptions']['locale'] = {
                            "format": "YYYY-MM-DD",
                            "applyLabel": "确定",
                            "cancelLabel": "取消",
                            "fromLabel": "起始时间",
                            "toLabel": "结束时间'",
                            "customRangeLabel": "自定义",
                            "weekLabel": "W",
                            "daysOfWeek": ["日", "一", "二", "三", "四", "五", "六"],
                            "monthNames": ["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"],
                            "firstDay": 1
                        };
                    }
                    if(field.type == "groupSelect"){
                        //ambiguityFields[field.name] = field;
                    }
                    if ( field.type == "uiSelect") {
                        //ambiguityFields.push(field);
                    }
                });
            }

            //定义grid数据集
            $scope.table.rows = [];
            //定义grid的Page对象
            $scope.table.page = {
                pageNumber: 1,
                pageSize: $scope.table.settings.pageSize || 10,
                prevPageDisabled: 'disabled',
                nextPageDisabled: 'disabled'
            };

            //date
            // Disable weekend selection


            //queryForm

            var autoLoad = $scope.table.settings.autoLoad;
            autoLoad = angular.isUndefined(autoLoad) ? true : autoLoad;
            if (!autoLoad) {
                $scope.table.loadingTip = "请查询!";
            } else {
                queryData();
            }
            //ajax异步获取数据
            function queryData() {
                $scope.loading = true;
                $scope.table.beforeReload && $scope.table.beforeReload();

                var queryParams = angular.copy($scope.table.queryParams);

                var checked = true;

                if($scope.table.params){
                    angular.forEach($scope.table.params, function (field) {
                        if(field.required && !queryParams[field.name]){
                            dbUtils.error(field.label+"为必填项!","提示");
                            $scope.loading = false;
                            checked = false;
                        }
                        //处理需要修改数据的model
                        if (field.type == 'date') {
                            var dt = $scope.table.queryParams[field.name];
                            dt && (queryParams[field.name] = dateFilter(dt, field['dateFormat']));
                            if(field.endDate && queryParams[field.name]){
                                queryParams[field.name] = queryParams[field.name] + " 23:59:59";
                            }
                        } else if (field.type == 'dateRange') {
                            var range = $scope.table.queryParams[field.name];
                            if (range) {
                                var dateRange = range.split(" - ");
                                queryParams[field['beginDateName'] || 'beginDate'] = dateRange[0];
                                queryParams[field['endDateName'] || 'endDate'] = dateRange[1]+' 23:59:59';
                            }
                        } else if (field.type == 'uiSelect') {
                            //$scope.table.queryParams[ambiguityField].value && (queryParams[field.name] = $scope.table.queryParams[ambiguityField].value);
                        } else if (field.type == 'groupSelect') {
                            //$scope.dbFormGrid.queryParams[field.name] && (queryParams[field.name] = $scope.dbFormGrid.queryParams[ambiguityField.name].key);
                        }
                    });
                };

                if(!checked){
                    return;
                }

                $scope.table.loadingTip = "正在努力为您加载数据,请稍候...";

                //分页参数在这里自行替换
                /*queryParams["page"] = {
                 keyWord: $scope.dbFormGrid.queryParams.keyWord || "",
                 pageNumber: $scope.dbFormGrid.page.pageNumber,
                 pageSize: $scope.dbFormGrid.page.pageSize
                 };*/
                queryParams['currentPage'] = $scope.table.page.pageNumber;
                queryParams['onePageSize'] = $scope.table.page.pageSize;
                dbUtils.get($scope.table.url, queryParams).then(function (response) {

                    //获取每行数据，并调用format方法进行处理，最后赋值给$scope.dbFormGrid.rows
                    $scope.loading = false;
                    if(response.code != SUCCESS_CODE){
                        dbUtils.warning("获取列表失败","提示");
                        return;
                    }
                    var data = response.data;
                    var _rows = data.data;
                    $scope.table._rows = _rows;
                    $scope.table.rows = [];
                    for (var i in _rows) {
                        var _row = $scope.table._rows[i];
                        var row = {};
                        for (var j in $scope.table.headers) {
                            var header = $scope.table.headers[j];
                            var value = _row[header.field] + '';
                            value = value || '';
                            if(header.formatter){
                                value = header.formatter(value,_row);
                            }
                            if(header.compile){
                                value = $sce.trustAsHtml(value);
                            }
                            row[header.field] = value;
                        }
                        //添加一个是否选择的字段，默认值为false
                        _row.checked = false;
                        $scope.table.rows.push(row);
                    }
                    //分页数据处理
                    var pages = {};
                    pages.totalRecords = data.totalResults;
                    pages.pageNumber = data.currentPage;
                    pages.pageSize = data.onePageSize;
                    pages.totalPages = data.totalPage;
                    var totalPage = pages.totalPages;

                    //分页算法，页面只显示固定数量的分页按钮。
                    var pageNumbers = [];
                    var startPage = 1;
                    var endPage = totalPage;
                    var pageStep = 2;//以当前页为基准，前后各显示的页数量
                    if (totalPage >= 6) {
                        startPage = pages.pageNumber;
                        if (startPage >= pageStep) {
                            startPage -= pageStep;
                        }
                        if (startPage <= 1) {
                            startPage = 1;
                        }
                        endPage = (totalPage - pages.pageNumber) >= pageStep ? pages.pageNumber + pageStep : totalPage;
                        if (endPage > totalPage) {
                            endPage = totalPage;
                        }
                        if (startPage != 1) {
                            pageNumbers.push({number: "1"});
                            if (startPage - 1 != 1) {
                                pageNumbers.push({number: "...", disabled: "disabled"});
                            }
                        }
                    }
                    for (var i = startPage; i <= endPage; i++) {
                        if (i == pages.pageNumber) {
                            pageNumbers.push({number: i, active: "active"});
                        } else {
                            pageNumbers.push({number: i});
                        }
                    }
                    if (endPage != totalPage) {
                        if (endPage + 1 != totalPage) {
                            pageNumbers.push({number: "...", disabled: "disabled"});
                        }
                        pageNumbers.push({number: totalPage});
                    }
                    pages.pageNumbers = pageNumbers;
                    if (pages.pageNumber == 1 || pages.totalPages == 0) {
                        pages.prevPageDisabled = "disabled";
                    }
                    if (pages.pageNumber == totalPage || pages.totalPages == 0) {
                        pages.nextPageDisabled = "disabled";
                    }
                    $scope.table.page = pages;
                    if (angular.isFunction($scope.table.onLoadFinish)) {
                        $scope.table.onLoadFinish($scope.table.rows);
                    }
                    // Metronic.stopPageLoading();

                    checkAllowSelect();
                    $scope.table.loadingTip = "未查询到数据";

                }, function () {
                    $scope.table.loadingTip = "未知异常，请稍后重试...";
                });
            }

            $scope.table.operations = {
                reloadData: function(){
                    $scope.table.page.pageNumber = 1;
                    queryData();
                },
                resetQuery: function(){
                    for(var p in $scope.table.queryParams){
                        $scope.table.queryParams[p] = '';
                    }
                },
                //分页数量点击事件
                pageNumberClick: function(pageNumber){
                    var prevPage = $scope.table.page.prevPageDisabled;
                    if (pageNumber === "prev" && prevPage && prevPage != "") {
                        return false;
                    }
                    var nextPage = $scope.table.page.nextPageDisabled;
                    if (pageNumber === "next" && nextPage && nextPage != "") {
                        return false;
                    }
                    if (pageNumber == $scope.table.page.pageNumber) {
                        return false;
                    }
                    if (pageNumber === "...") {
                        return false;
                    }
                    if (pageNumber === "prev") {
                        $scope.table.page.pageNumber--;
                    } else if (pageNumber === "next") {
                        $scope.table.page.pageNumber++;
                    } else {
                        $scope.table.page.pageNumber = pageNumber;
                    }
                    queryData();
                },
                //grid上方按钮点击事件
                operationButtonClick: function(clickFun){
                    var rows = getAllSelectRows();
                    clickFun(rows);
                    checkAllowSelect();
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
                setQueryParam: function(fieldName, value){
                    $scope.table.queryParams[fieldName] = value;
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

            //目前设计无法进行排序，会导致展示的row和实际的row不能对应
            /*$scope.table.sorting = function (header) {
                console.log(header);
                angular.forEach($scope.tables.headers, function (h) {
                    if (angular.equals(h.field, header.field)) {
                        if (angular.isUndefined(header.sortingClass)) {
                            h.sortingClass = "sorting_asc";
                        } else if (h.sortingClass == "sorting_asc") {
                            h.sortingClass = "sorting_desc";
                        } else {
                            h.sortingClass = "sorting_asc";
                        }
                    } else {
                        h.sortingClass = "sorting_both";
                    }
                });
                // 执行排序功能
                if (header.sortingClass == "sorting_desc") {
                    $scope.table.rows = descSort($scope.table.rows, header.field);
                } else {
                    $scope.dbFormGrid.showRows = ascSort($scope.dbFormGrid.showRows, header.field);
                }

                function ascSort(json, key) {
                    return json.sort(function (a, b) {
                        var x = a[key];
                        var y = b[key];
                        return ((x < y) ? -1 : ((x > y) ? 1 : 0));
                    });
                }

                function descSort(json, key) {
                    return json.sort(function (a, b) {
                        var x = a[key];
                        var y = b[key];
                        return ((x > y) ? -1 : ((x < y) ? 1 : 0));
                    });
                }

            }*/


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