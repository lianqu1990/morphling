/**
 * Created by hanchao on 2016/11/8.
 */
'use strict';

angular.module("app")
    .directive("uiNav", ['$rootScope', function ($rootScope) {
        //这里规则可能会特别复杂
        var $menus = [];
        var _tpl = "";
        if($rootScope && $rootScope._userinfo){
            $menus = $rootScope._userinfo.menus;

            for (var i = 0; i < $menus.length; i++) {
                _tpl += _buildBranchTemplate($menus[i]);
            }
        }

        return {
            template: "<ul class=\"nav\">" + _tpl + "</ul>"
        }

        /**
         * 构建父菜单的html
         * @param node
         * @returns {string}
         * @private
         */
        function _buildBranchTemplate(node) {
            if (node.type == 1 && !node.children ) {
                return "<li ui-sref-active=\"active\">" +
                    "<a ui-sref=\"" + node.route + "\">" +
                    "<i class=\"" + node.icon + "\"></i>" +
                    "<span  " + ((node.translate)?("translate=\""+node.translate+"\""):"")+">"+node.text+"</span>" +
                    "</a>" +
                    "</li>";
            } else if (node.type == 1) {
                var _tpl = "";
                _tpl += "<li >" +
                    "<a href class=\"auto\">" +
                    "<span class=\"pull-right text-muted\">" +
                    "<i class=\"fa fa-fw fa-angle-right text\"></i>" +
                    "<i class=\"fa fa-fw fa-angle-down text-active\"></i>" +
                    "</span>" +
                    "<i class=\"" + node.icon + "\"></i>" +
                    "<span  " + ((node.translate)?("translate=\""+node.translate+"\""):"")+ ">" + node.text + "</span>" +
                    "</a>" +
                    "<ul class=\"nav nav-sub dk\">" +
                    "<li class=\"nav-sub-header\">" +
                    "<a href>" +
                    "<span  " + ((node.translate)?("translate=\""+node.translate+"\""):"")+ ">" + node.text + "</span>" +
                    "</a>" +
                    "</li>";
                var _childrenTpl = "";
                for (var i = 0; i < node.children.length; i++) {
                    _childrenTpl += _buildChildrenTemplate(node.children[i]);
                }
                _tpl += _childrenTpl;
                _tpl += "</ul></li>"
                return _tpl;
            } else if (node.type == 2) {
                return "<li class=\"hidden-folded padder m-t m-b-sm text-muted text-xs\">" +
                    "<span  " + ((node.translate)?("translate=\""+node.translate+"\""):"")+ ">" + node.text + "</span>" +
                    "</li>";
            } else if (node.type == 3) {
                return "<li class=\"line dk\"></li>";
            } else if (node.type == 4) {
                return node.html;
            }else{
                 return "";
            }
        }

        /**
         * 构建子菜单的html
         * @param node
         * @returns {*}
         * @private
         */
        function _buildChildrenTemplate(node) {
            if (node.type != 0) {
                return _buildBranchTemplate(node);
            } else {
                var _tpl = "<li ui-sref-active=\"active\">" +
                    "<a ui-sref=" + node.route + ">" +
                    "<span  " + ((node.translate)?("translate=\""+node.translate+"\""):"")+ ">" + node.text + "</span>" +
                    "</a></li>";
                return _tpl;
            }
        }

    }]);