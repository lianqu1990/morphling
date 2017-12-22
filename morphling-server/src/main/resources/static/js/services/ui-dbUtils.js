var dbUtils = angular.module("dbUtils", []);

dbUtils.factory("dbUtils", ["$http", "$window", "$q", "toaster", "$modal",'$state', DbFetch]);

function DbFetch($http, $window, $q, toaster, $modal,$state) {
    var promise = false;

    function getHtml(content, type) {
        var html = [];
        html.push('<div class="modal-header">');
        html.push('<h3 class="modal-title">提示</h3>');
        html.push('</div>');
        html.push('<div class="modal-body">');
        html.push(content);
        html.push('</div>');
        html.push('<div class="modal-footer">');
        html.push('<button class="btn btn-primary" type="button" ng-click="ok()">确定</button>');
        if (type == "confirm") {
            html.push('<button class="btn btn-warning" type="button" ng-click="cancel()">取消</button>');
        }
        html.push('</div>');
        return html.join('');
    }

    function DialogController($scope, $modalInstance) {
        $scope.ok = function () {
            $modalInstance.close('cancel');
        };

        $scope.cancel = function () {
            $modalInstance.dismiss('cancel');
        }
    }

    function dbAlert(content, okFun,size) {
        $modal.open({
            animation: true,
            template: getHtml(content),
            controller: ['$scope', '$modalInstance', DialogController],
            size: size? size : "sm",
            backdrop: "static"
        }).result.then(function () {
            if (angular.isFunction(okFun)) {
                okFun.call();
            }
        });
    }



    function showImg(src){
        var html = [];
        html.push('<div class="modal-body">');
        html.push('<i ng-click="ok()" class="icon-close" style="margin:-15px -15px auto auto;margin-bottom:5px;font-size:30px;color:#131e25;float:right;"></i>');
        html.push('<img class="img-rounded img-responsize" src="'+src+'" style="width:100%" />');
        html.push('</div>');

        $modal.open({
            animation: true,
            template: html.join(""),
            controller: ['$scope', '$modalInstance', DialogController]
        })
    }

    function doTip(content) {
        var html = [];
        html.push('<div class="modal-header">');
        html.push('<h3 class="modal-title">提示</h3>');
        html.push('<div class="modal-body">');
        html.push(content);
        html.push('</div>');
        html.push('</div>');
        html.push('<div class="modal-footer">');
        html.push('</div>');

        $modal.open({
            animation: true,
            template: html.join(""),
            controller: ['$scope', '$modalInstance', DialogController],
            size: "sm"
        })
    }

    function doToasterTip(type, title, content) {
        toaster.pop({
            type: type,
            title: title,
            body: content,
            timeout: 2000,
            bodyOutputType: 'trustedHtml'
        });
    }

    return {
        success: function (content, title) {
            doToasterTip('success', title, content);
        },
        info: function (content, title) {
            doToasterTip('info', title, content);
        },
        warning: function (content, title) {
            doToasterTip('warning', title, content);
        },
        error: function (content, title) {
            doToasterTip('error', title, content);
        },
        alert: function (content, okFun) {
            dbAlert(content, okFun);
        },
        tip: function (content) {
            doToasterTip(content);
        },
        alertInfo: function(content){
            doTip(content);
        },
        confirm: function (content, okFun, cancerFun) {
            $modal.open({
                animation: true,
                template: getHtml(content, 'confirm'),
                controller: ['$scope', '$modalInstance', DialogController],
                size: "sm",
                backdrop: "static"
            }).result.then(function () {
                okFun.call();
            }, function () {
                if (angular.isFunction(cancerFun)) {
                    cancerFun.call();
                }
            });
        },
        showImg: function(src){
            showImg(src);
        },
        post: function (transCode, data) {
            var deferred = $q.defer();
            // var apiUrl = "http://hp.xiqing.info/mobile/api.do";
            // var apiUrl = "http://localhost:8080/mobile/api.do";
            $http({
                method: "POST",
                url: transCode,
                data: data,
                headers: {'Content-Type': 'application/x-www-form-urlencoded','Accept': 'application/json;charset=utf-8'},
                transformRequest: function (obj) {
                    var str = [];
                    for (var p in obj) {
                        str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
                    }
                    return str.join("&");
                }
            }).then(
                function (data, status) {
                    deferred.resolve(data.data);
                },
                function (data, status) {
                    if(data['status'] == 401){
                        $state.go("access.signin");
                        return;
                    }
                    doToasterTip('warning', "warning", (data.data && data.data.message) ? data.data.message : "网络通讯异常，请稍后再试！");
                    console.log("网络通讯异常，请稍后再试！");
                    deferred.reject(data.data);
                }
            );
            return deferred.promise;
        },
        put: function (transCode, data) {
            var deferred = $q.defer();
            // var apiUrl = "http://hp.xiqing.info/mobile/api.do";
            // var apiUrl = "http://localhost:8080/mobile/api.do";
            $http({
                method: "PUT",
                url: transCode,
                data: data,
                headers: {'Content-Type': 'application/x-www-form-urlencoded','Accept': 'application/json;charset=utf-8'},
                transformRequest: function (obj) {
                    var str = [];
                    for (var p in obj) {
                        str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
                    }
                    return str.join("&");
                }
            }).then(
                function (data, status) {
                    deferred.resolve(data.data);
                },
                function (data, status) {
                    if(data['status'] == 401){
                        $state.go("access.signin");
                        return;
                    }
                    doToasterTip('warning', "warning", (data.data && data.data.message) ? data.data.message : "网络通讯异常，请稍后再试！");
                    console.log("网络通讯异常，请稍后再试！");
                    deferred.reject(data.data);
                }
            );
            return deferred.promise;
        },
        delete: function (transCode, data) {
            var deferred = $q.defer();
            if (data) {
                var str = [];
                for (var p in data) {
                    str.push(encodeURIComponent(p) + "=" + encodeURIComponent(data[p]));
                }
                transCode = transCode + "?" + str.join("&");
            }
            $http({
                method: "DELETE",
                url: transCode,
                data: data,
                headers: {'Content-Type': 'application/x-www-form-urlencoded','Accept': 'application/json;charset=utf-8'},
            }).then(
                function (data, status) {
                    deferred.resolve(data.data);
                },
                function (data, status) {
                    if(data['status'] == 401){
                        $state.go("access.signin");
                        return;
                    }
                    doToasterTip('warning', "warning", (data.data && data.data.message) ? data.data.message : "网络通讯异常，请稍后再试！");
                    console.log("网络通讯异常，请稍后再试！");
                    deferred.reject(data.data);
                }
            );
            return deferred.promise;
        },
        postBody: function (transCode, data) {
            var deferred = $q.defer();
            $http({
                method: "POST",
                url: transCode,
                data: data,
                headers: {'Content-Type': 'application/json','Accept': 'application/json;charset=utf-8'}
            }).then(
                function (data, status) {
                    deferred.resolve(data.data);
                },
                function (data, status) {
                    if(data['status'] == 401){
                        $state.go("access.signin");
                        return;
                    }
                    doToasterTip('warning', "warning", (data.data && data.data.message) ? data.data.message : "网络通讯异常，请稍后再试！");
                    console.log("网络通讯异常，请稍后再试！");
                    deferred.reject(data.data);
                }
            );
            return deferred.promise;

        },
        putBody: function (transCode, reqBody) {
            var deferred = $q.defer();
            var ApiRequest = {};
            ApiRequest["transCode"] = transCode;
            ApiRequest["requestBody"] = reqBody;

            // var apiUrl = "http://hp.xiqing.info/mobile/api.do";
            // var apiUrl = "http://localhost:8080/mobile/api.do";
            $http({
                headers: {'Content-Type': 'application/x-www-form-urlencoded','Accept': 'application/json;charset=utf-8'}
            }).post(transCode, ApiRequest).then(
                function (data, status) {
                    deferred.resolve(data.data);
                },
                function (data, status) {
                    if(data['status'] == 401){
                        $state.go("access.signin");
                        return;
                    }
                    doToasterTip('warning', "warning", (data.data && data.data.message) ? data.data.message : "网络通讯异常，请稍后再试！");
                    console.log("网络通讯异常，请稍后再试！");
                    deferred.reject(data.data);
                }
            );
            return deferred.promise;

        },
        get: function (transCode, data) {
            var deferred = $q.defer();

            if (data) {
                var str = [];
                for (var p in data) {
                    str.push(encodeURIComponent(p) + "=" + encodeURIComponent(data[p]));
                }
                transCode = transCode + "?" + str.join("&");
            }
            $http.get(transCode,{
                headers: {'Content-Type': 'application/x-www-form-urlencoded','Accept': 'application/json;charset=utf-8'}
            }).then(
                function (data, status) {
                    deferred.resolve(data.data);
                },
                function (data, status) {
                    if(data['status'] == 401){
                        $state.go("access.signin");
                        return;
                    }
                    doToasterTip('warning', "warning", (data.data && data.data.message) ? data.data.message : "网络通讯异常，请稍后再试！");
                    console.log("网络通讯异常，请稍后再试！");
                    deferred.reject(data.data);
                }
            );
            return deferred.promise;
        },
        dateFormat: function (date) {
            var year = date.getFullYear();
            var month = date.getMonth() + 1;
            var date = date.getDate();
            return year + "-" + (month > 9 ? month : ("0" + month)) + "-" + (date > 9 ? date : ("0" + date));
        },

        /*
         * 根据指定变量获取集合中此变量的数组数据返回.
         * @param name
         * @param rows
         * @returns {Array}
         */
        getFieldArray: function (objectList, name) {
            var data = [];
            angular.forEach(objectList, function (record) {
                data.push(record[name]);
            });
            return data;
        },
        toString: function(obj,replace){
            var result = JSON.stringify(obj,null,"\t");
            if(replace){
                result = result.replace( /\t/g,"&nbsp;&nbsp;&nbsp;&nbsp;");
                result = result.replace( /\n/g,"<br/>");
            }
            return result;
        },
        showJson: function(str){
            str = "<pre>"+str+"</pre>"
            dbAlert(str,null,"lg");
        }
    }
}
