'use strict';

angular.module("app").service('AccountService', ['dbUtils', '$http', '$q','$cookies', function (dbUtils, $http, $q,$cookies) {
    return {
        getValidImgCode: function () {
            return "/validcode/img?" + new Date().getTime();
        },
        login: function (user) {
            /**/
            return dbUtils.post("/auth/login", {
                username: user.username,
                password: user.password,
                captcha: user.captcha,
                rememberMe: user.rememberMe?true:false
            });
        },
        register: function(data){
            return dbUtils.post("/auth/register",data);
        },
        getAccount: function(success,fail){
            //return dbUtils.get("/account/get");
            jQuery.ajax({
                url: "/auth/get",
                async: false,
                dataType: 'JSON',
                success:function(response){
                    success(response);
                },
                error:function(xhr,textStatus){
                    fail(xhr,textStatus);
                }
            });
        },

        logout: function(){
            return dbUtils.get("/auth/logout");
        },
        sendCode: function(type,data){
            return dbUtils.post("/validcode/phone/"+type, data);
        },
        checkCode:function(type,phone,code){
            return dbUtils.post("/validcode/phone/check/"+type,{phone:phone,code:code});
        },
        resetPass: function(data){
            return dbUtils.post("/auth/reset",data);
        },
        modifypwd: function(data){
            return dbUtils.post("/auth/modify",data);
        },
        switchEnv: function(envs,env){
            var expireDate = new Date();
            expireDate.setDate(expireDate.getDate() + 30);
            $cookies.put("env",env,{'expires':expireDate.toUTCString()});
            return this.getEnv(envs);
        },
        getEnv: function(envs){
            var env = $cookies.get("env");
            var valid = false;
            for(var i in envs){
                if(envs[i].key == env){
                    valid = true;
                    break;
                }
            }
            if(valid){
                return env;
            }else{
                var expireDate = new Date();
                expireDate.setDate(expireDate.getDate() + 30);
                $cookies.put("env",envs[0].key,{'expires':expireDate.toUTCString()});
                return envs[0].key;
            }
        }
    }
}]);





// switchEnv: function(envs,env){
//     $localStorage.env = env;
//     return this.getEnv(envs);
// },
// getEnv: function(envs){
//     var env = $localStorage.env;
//     var valid = false;
//     for(var i in envs){
//         if(envs[i].key == env){
//             valid = true;
//             break;
//         }
//     }
//     if(valid){
//         return env;
//     }else{
//         $localStorage.env = envs[0];
//         return envs[0].key;
//     }
// }