'use strict';

/**
 * Config for the router
 */
angular.module('app')
    .run(
        ['$rootScope', '$state', '$stateParams', '$cookies', '$cookieStore', 'AccountService',
            function ($rootScope, $state, $stateParams, $cookies, $cookieStore, accountService) {
                $rootScope.$state = $state;   //方便获取状态
                $rootScope.$stateParams = $stateParams;    //方便获取状态

                $rootScope.$on('$stateChangeStart', function (event, toState, toParams, fromState) {
                    if (toState.name == 'app.logout') {
                        accountService.logout().then(function () {
                            //$state.go("access.signin", {from: fromState.name, w: 'logout'});
                            window.location.href = "/";
                        });
                    }
                    if (toState.name == 'access.signin' || toState.name == 'access.forgotpwd' || toState.name == 'access.resetpwd') {
                        return;// 如果是进入登录界面则允许
                    }

                    //请求异步导致下面执行出错，可优化,
                    //换成了jquery的
                    if (!$rootScope._userinfo) { //授权页面
                        accountService.getAccount(function (response) {
                            if (response && response.code == SUCCESS_CODE) {
                                $rootScope._userinfo = response.data;
                                $rootScope._userinfo.currentEnv = accountService.getEnv($rootScope._userinfo.envs);
                            }
                        }, function () {
                            //请求失败
                            return;
                        });
                    }
                    if ($rootScope._userinfo) {
                        return;
                    } else {
                        event.preventDefault();
                        $state.go("access.signin", {from: fromState.name, w: 'notLogin'});//跳转到登录界面
                    }

                });
            }
        ]
    )
    .config(
        ['$stateProvider', '$urlRouterProvider',
            function ($stateProvider, $urlRouterProvider) {

                $urlRouterProvider.otherwise('/app/home');
                $stateProvider
                    .state('app', {
                        abstract: true,
                        url: '/app',
                        templateUrl: 'tpl/app.html'  //app.html是搭建框架的，相当于sitemesh
                    })
                    .state('app.home', {
                        url: '/home',
                        templateUrl: 'tpl/user/home.html',
                        resolve: {
                            deps: ['$ocLazyLoad',
                                function ($ocLazyLoad) {
                                    return $ocLazyLoad.load(['js/controllers/home.js', 'js/services/baseinfo.js']);
                                }]
                        }
                    })
                    .state('app.auth', {
                        url: '/auth',
                        templateUrl: 'tpl/user/auth.html',
                        resolve: {
                            deps: ['$ocLazyLoad',
                                function ($ocLazyLoad) {
                                    return $ocLazyLoad.load(['js/controllers/auth.js', 'js/services/cardaccount.js']);
                                }]
                        }
                    })
                    .state('app.logout', {
                        url: '/logout',
                        template: '<div ui-view class="fade-in-up"></div>'
                    })
                    .state('app.modifypwd', {
                        url: '/modifypwd',
                        templateUrl: 'tpl/access/page_modifypwd.html',
                        resolve: {
                            deps: ['$ocLazyLoad',
                                function ($ocLazyLoad) {
                                    return $ocLazyLoad.load(['js/controllers/modifypwd.js']);
                                }]
                        }
                    })
                    .state('app.personalinfo', {
                        url: '/personalinfo',
                        templateUrl: 'tpl/user/personalinfo.html',
                        resolve: {
                            deps: ['$ocLazyLoad',
                                function ($ocLazyLoad) {
                                    return $ocLazyLoad.load(['js/controllers/personalinfo.js', 'js/services/user.js', 'js/services/baseinfo.js']);
                                }]
                        }
                    })
                    .state('app.selectApp', {
                        url: '/selectApp?from',
                        templateUrl: 'tpl/user/select_app.html',
                        resolve: {
                            deps: ['$ocLazyLoad',
                                function ($ocLazyLoad) {
                                    return $ocLazyLoad.load(['js/controllers/selectApp.js', 'js/services/application.js']);
                                }]
                        }
                    })
                    .state('app.degrade', {
                        url: '/degrade/:appId',
                        templateUrl: 'tpl/degrade/degrade_manage.html',
                        resolve: {
                            deps: ['$ocLazyLoad',
                                function ($ocLazyLoad) {
                                    return $ocLazyLoad.load(['js/controllers/degradeManage.js', 'js/services/application.js','js/services/degrade.js']);
                                }]
                        }
                    })
                    .state('app.cache', {
                        url: '/cache/:appId',
                        templateUrl: 'tpl/cache/cache_manage.html',
                        resolve: {
                            deps: ['$ocLazyLoad',
                                function ($ocLazyLoad) {
                                    return $ocLazyLoad.load(['js/controllers/cacheManage.js', 'js/services/application.js','js/services/cache.js']);
                                }]
                        }
                    })
                    .state('app.deploy', {
                        url: '/deploy/:appId',
                        templateUrl: 'tpl/deploy/deploy_app.html',
                        resolve: {
                            deps: ['$ocLazyLoad',
                                function ($ocLazyLoad) {
                                    return $ocLazyLoad.load(['js/controllers/deploy.js', 'js/services/application.js','js/services/deploy.js']);
                                }]
                        }
                    })


                    .state('access', {
                        url: '/access',
                        template: '<div ui-view class="fade-in-right-big smooth"></div>'
                    })
                    .state('access.signin', {
                        url: '/signin',
                        templateUrl: 'tpl/access/page_signin.html',
                        resolve: {
                            deps: ['uiLoad',
                                function (uiLoad) {
                                    return uiLoad.load(['js/controllers/signin.js']);
                                }]
                        }
                    })
                    .state('access.forgotpwd', {
                        url: '/forgotpwd',
                        templateUrl: 'tpl/access/page_forgotpwd.html',
                        resolve: {
                            deps: ['uiLoad',
                                function (uiLoad) {
                                    return uiLoad.load(['js/controllers/forgotpwd.js', 'js/controllers/imgvalid.js']);
                                }]
                        }
                    })
                    .state('access.resetpwd', {
                        url: '/resetpwd?{phone:1[345789][0-9]{9}}&code',//
                        templateUrl: 'tpl/access/page_resetpwd.html',
                        resolve: {
                            deps: ['uiLoad',
                                function (uiLoad) {
                                    return uiLoad.load(['js/controllers/resetpwd.js']);
                                }]
                        }
                    })
                    .state('system', {
                        url: '/system',
                        templateUrl: 'tpl/app.html'
                    })
                    .state('system.client', {
                        url: '/client',
                        templateUrl: 'tpl/system/client_list.html',
                        resolve: {
                            deps: ['uiLoad',
                                function (uiLoad) {
                                    return uiLoad.load(['js/controllers/client.js', 'js/services/client.js']);
                                }]
                        }
                    })
                    .state('system.users', {
                        url: '/users',
                        templateUrl: 'tpl/system/user_list.html',
                        resolve: {
                            deps: ['uiLoad',
                                function (uiLoad) {
                                    return uiLoad.load(['js/controllers/user.js', 'js/services/user.js','js/services/role.js','js/services/application.js']);
                                }]
                        }
                    })
                    .state('system.app', {
                        url: '/app',
                        templateUrl: 'tpl/app/app_list.html',
                        resolve: {
                            deps: ['uiLoad',
                                function (uiLoad) {
                                    return uiLoad.load(['js/controllers/application.js', 'js/services/application.js']);
                                }]
                        }
                    })
                    .state('system.instance', {
                        url: '/{appId:\\d+}/instance',
                        templateUrl: 'tpl/app/instance_admin.html',
                        resolve: {
                            deps: ['uiLoad',
                                function (uiLoad) {
                                    return uiLoad.load(['js/controllers/application.js', 'js/services/application.js']);
                                }]
                        }
                    })
            }
        ]
    );