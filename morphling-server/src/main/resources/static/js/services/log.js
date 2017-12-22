var log = angular.module("EchoLog", []);
log.factory("EchoLog", ["$http", "$window", "$q", "toaster", "$modal",'$interval', function ($http, $window, $q, toaster, $modal,$interval){
    function LogController ($scope,$modalInstance,logId){
        $scope.ok = function () {
            $modalInstance.close('cancel');
        };
        var websocket = new WebSocket("ws://"+window.location.host+"/echo?logId="+logId);

        var printing = false;

        var logInternal = setInterval(function(){
            var e = angular.element("#log_container")[0];

            var tmp = e.innerHTML;
            if(tmp.length > 100000){
                tmp = tmp.substring(50000,tmp.length);
            }
            e.innerHTML=tmp;
            if(printing && angular.element("#scrollControl")[0].checked){
                e.scrollTop=e.scrollHeight;
                printing = false;
            }
        },200);
        websocket.onopen = function(event) {

        }


        // 监听消息
        websocket.onmessage = function(event) {
            printing = true;
            log(event.data);
        };

        // 监听Socket的关闭
        websocket.onclose = function(event) {
            window.clearInterval(logInternal);
            $scope.$apply(function(){
                $scope.finished = true;
            })
        };


    }


    function log(line){
        var e = angular.element("#log_container")[0];
        e.innerHTML = e.innerHTML + line + "<br/>";

    }

    return {
        show:function(logId,okFun){
            $modal.open({
                animation: true,
                templateUrl: "tpl/log/log.html",
                controller: ['$scope', '$modalInstance','logId', LogController],
                size: "lg",
                backdrop: "static",
                keyboard: false,
                resolve: {
                    logId:function(){
                        return logId+"";
                    }
                }
            }).result.then(function () {
                if (angular.isFunction(okFun)) {
                    okFun.call();
                }
            });
        }
    }
}]);
