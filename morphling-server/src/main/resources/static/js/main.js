'use strict';

/* Controllers */

angular.module('app').controller('AppCtrl', ['$rootScope','$scope', '$translate', '$localStorage', '$state','$window','$modal','AccountService','dbUtils', function ($rootScope,$scope, $translate, $localStorage, $state,$window,$modal,accountService,dbUtils) {
    // add 'ie' classes to html
    var isIE = !!navigator.userAgent.match(/MSIE/i);
    isIE && angular.element($window.document.body).addClass('ie');
    isSmartDevice($window) && angular.element($window.document.body).addClass('smart');

    // config
    $scope.app = {
        name: 'morphling',//
        version: '1.0.0',
        // for chart colors
        color: {
            primary: '#7266ba',
            info: '#23b7e5',
            success: '#27c24c',
            warning: '#fad733',
            danger: '#f05050',
            light: '#e8eff0',
            dark: '#3a3f51',
            black: '#1c2b36'
        },
        settings: {
            themeID: 1,
            navbarHeaderColor: 'bg-black',
            navbarCollapseColor: 'bg-white-only',
            asideColor: 'bg-black',
            headerFixed: true,
            asideFixed: false,
            asideFolded: false,
            asideDock: false,
            container: false
        }
    }

    // save settings to local storage
    if (angular.isDefined($localStorage.settings)) {
        $scope.app.settings = $localStorage.settings;
    } else {
        $localStorage.settings = $scope.app.settings;
    }
    $scope.$watch('app.settings', function () {
        if ($scope.app.settings.asideDock && $scope.app.settings.asideFixed) {
            // aside dock and fixed must set the header fixed.
            $scope.app.settings.headerFixed = true;
        }
        // save to local storage
        $localStorage.settings = $scope.app.settings;
    }, true);

    // angular translate
    $scope.lang = {isopen: false};
    //语言设置
    $scope.langs = {cn: 'Chinese', en: 'English', de_DE: 'German', it_IT: 'Italian'};
    $scope.selectLang = $scope.langs[$translate.proposedLanguage()] || "English";
    $scope.setLang = function (langKey, $event) {
        // set the current lang
        $scope.selectLang = $scope.langs[langKey];
        // You can change the language during runtime
        $translate.use(langKey);
        $scope.lang.isopen = !$scope.lang.isopen;
    };

    function isSmartDevice($window) {
        // Adapted from http://www.detectmobilebrowsers.com
        var ua = $window['navigator']['userAgent'] || $window['navigator']['vendor'] || $window['opera'];
        // Checks for iOs, Android, Blackberry, Opera Mini, and Windows mobile devices
        return (/iPhone|iPod|iPad|Silk|Android|BlackBerry|Opera Mini|IEMobile/).test(ua);
    }
    
    
    $scope.switchEnv = function (key) {
        if(key == $rootScope._userinfo.currentEnv){
            return;
        }
        var currentEnv = accountService.switchEnv($rootScope._userinfo.envs,key);
        var envName = "";
        for(var i in $rootScope._userinfo.envs){
            if($rootScope._userinfo.envs[i].key == currentEnv){
                envName = $rootScope._userinfo.envs[i].name;
                break;
            }
        }
        $state.go("app.home");
        alert("操作成功，环境已经切换为【"+envName+"】!");
        // dbUtils.alert("<span class='text-danger'>【"+envName+"】</span> ！",function(){
        //     //刷新页面
        // });
        setTimeout(function(){
            window.location.reload();
        },200);
    };

}]);

