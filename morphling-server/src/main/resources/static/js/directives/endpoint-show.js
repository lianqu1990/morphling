'use strict';
app.controller('EndpointShowController', ['dbUtils', '$scope', function (dbUtils, $scope) {
    $scope.showEndpoint = function () {
        dbUtils.showJson($scope.endpointDetails);
    }
}])
app.directive('endpointShow', ['dbUtils', '$compile', function (dbUtils, $compile) {
    return {
        restrict: 'EA',
        template: "<span ng-controller='EndpointShowController'  data-html=\"true\" tooltip-placement=\"left\" tooltip-html-unsafe=\"{{endpointEllipsis}}\"  ng-click='showEndpoint()'>" +
        "<span ng-transclude></span>" +
        "</span>",
        replace: true,
        transclude: true,
        link: function (scope, element, attrs, controller) {
            function init() {
                var details = attrs["endpointShow"];
                if (!details || details.length == 0) {
                    setTimeout(init, 200);
                    return;
                }
                if (details && details.length > 300) {
                    scope.endpointEllipsis = details.substring(0, 300) + "......";
                } else {
                    scope.endpointEllipsis = details;
                }
                scope.endpointDetails = details;
            }

            init();
        }
    }

}]);