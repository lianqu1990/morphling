package com.huatu.morphling.service.local;

import com.google.common.collect.Lists;
import com.huatu.morphling.bean.UserInfo;
import com.huatu.morphling.common.consts.SecurityConsts;
import com.huatu.morphling.dao.jpa.api.EnvDao;
import com.huatu.morphling.dao.jpa.api.MenuDao;
import com.huatu.morphling.dao.jpa.api.RoleDao;
import com.huatu.morphling.dao.jpa.entity.Env;
import com.huatu.morphling.dao.jpa.entity.Menu;
import com.huatu.morphling.dao.jpa.entity.Role;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author hanchao
 * @date 2017/11/6 13:59
 */
@Service
public class RoleService extends BaseService<Role,Integer> {
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private MenuDao menuDao;
    @Autowired
    private EnvDao envDao;
    @Override
    public JpaRepository<Role, Integer> getDefaultDao() {
        return roleDao;
    }

    public List<Role> findByUserId(int userId){
        return roleDao.findByUserId(userId);
    }

    public List<UserInfo.Menu> findMenuByRole(List<Role> roles){
        //List<Menu> menus = menuDao.findByRole(Lists.newArrayList(ArrayUtils.toObject(roleId)));
        List<Menu> menus;
        if(roles.stream().anyMatch(role -> SecurityConsts.ROLE_SUPER_ADMIN.equals(role.getRole()))){
            menus = menuDao.findAll();
        }else{
            menus = menuDao.findByRole(roles.stream().map(Role::getId).collect(Collectors.toList()));
        }
        return transformMenu(menus);
    }

    public List<UserInfo.Env> findEnvByRole(List<Role> roles){
        List<Env> envs;
        if(CollectionUtils.isEmpty(roles)){
            return Lists.newArrayList();
        }
        if(roles.stream().anyMatch(role -> SecurityConsts.ROLE_SUPER_ADMIN.equals(role.getRole()))){
            envs = envDao.findAll();
        }else{
            envs = envDao.findByRole(roles.stream().map(Role::getId).collect(Collectors.toList()));
        }
        return transformEnv(envs);
    }

    public List<UserInfo.Env> transformEnv(List<Env> envs){
        return envs.stream().map(env -> {
            return UserInfo.Env.builder()
                    .key(env.getKey())
                    .name(env.getName())
                    .prod(env.isProd())
                    .build();
        }).collect(Collectors.toList());
    }


    private List<UserInfo.Menu> transformMenu(List<Menu> menus){
        List<UserInfo.Menu> result = Lists.newArrayList();
        Map<Integer, UserInfo.Menu> menuMap = menus.stream()
                .map(menu -> {
                    return UserInfo.Menu.builder()
                            .id(menu.getId())
                            .text(menu.getText())
                            .type(menu.getType())
                            .icon(menu.getIcon())
                            .route(menu.getRoute())
                            .html(menu.getHtml())
                            .translate(menu.getTranslate())
                            .parent(menu.getParent())
                            .build();
                })
                .collect(Collectors.toMap(UserInfo.Menu::getId, Function.identity()));

        for (UserInfo.Menu menu : menuMap.values()) {
            if(menu.getParent() == 0){
                result.add(menu);
            }else{
                menuMap.get(menu.getParent()).addChildren(menu);
            }
        }
        return result;
    }
}
