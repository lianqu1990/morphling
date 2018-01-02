package com.lianqu1990.morphling.service.local;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;
import com.lianqu1990.morphling.common.consts.SecurityConsts;
import com.lianqu1990.morphling.dao.jpa.api.MenuDao;
import com.lianqu1990.morphling.dao.jpa.api.RoleDao;
import com.lianqu1990.morphling.dao.jpa.entity.Menu;
import com.lianqu1990.morphling.dao.jpa.entity.Role;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author hanchao
 * @date 2017/10/22 19:12
 */
@Service
public class MenuService extends BaseService<Menu,Integer> implements InitializingBean {
    @Autowired
    private MenuDao defaultDao;
    @Autowired
    private RoleDao roleDao;

    private static Map<Integer,Menu> menus;

    @Override
    public JpaRepository<Menu, Integer> getDefaultDao() {
        return defaultDao;
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        menus = findAll().stream().collect(Collectors.toMap(Menu::getId, Function.identity()));
    }

    public Map<Integer, Menu> getMenus() {
        return menus;
    }

    public SetMultimap<String,String> getUrlRoleMapping(){
        SetMultimap<String,String> roleMapping = HashMultimap.create();
        List<Menu> menuList = findAll();
        for (Menu menu : menuList) {
            String[] urls = menu.getUrlMatches().split(",");
            List<String> roles = roleDao.findByMenu(menu.getId()).stream().map(Role::getRole).collect(Collectors.toList());
            roles.add(SecurityConsts.ROLE_SUPER_ADMIN);
            for (String url : urls) {
                if(StringUtils.isNotBlank(url)){
                    roleMapping.putAll(url,roles);
                }
            }
        }
        return roleMapping;
    }
}
