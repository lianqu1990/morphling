package com.lianqu1990.morphling.bean;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hanchao
 * @date 2017/10/22 15:00
 */
@Data
@Builder
public class UserInfo {
    private int id;
    private String username;
    private String name;
    private String email;
    private String headImg;
    private List<String> roles;
    private List<Menu> menus;
    private List<Env> envs;

    @Data
    @Builder
    public static class Menu {
        private Integer id;
        private String text;
        private int type;
        private String icon;
        private String route;
        private String html;
        private String translate;
        private int parent;
        private List<Menu> children;

        public Menu addChildren(Menu menu){
            if(this.children == null){
                this.children = new ArrayList<>();
            }
            this.children.add(menu);
            return this;
        }
    }


    @Data
    @Builder
    public static class Env {
        private String key;
        private String name;
        private boolean prod;
    }

}
