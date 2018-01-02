package com.lianqu1990.morphling.service.local;

import com.lianqu1990.morphling.bean.UserInfo;
import com.lianqu1990.morphling.spring.security.SecurityUser;
import com.lianqu1990.morphling.dao.jpa.api.RoleDao;
import com.lianqu1990.morphling.dao.jpa.api.UserDao;
import com.lianqu1990.morphling.dao.jpa.entity.Role;
import com.lianqu1990.morphling.dao.jpa.entity.User;
import com.lianqu1990.morphling.spring.security.UserDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author hanchao
 * @date 2017/10/23 21:55
 */
@Service
@Slf4j
public class UserDetailsServiceImpl extends BaseService<User,Integer> implements UserDetailsService {

    @Autowired
    private UserDao userDao;
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleService roleService;



    @Override
    public JpaRepository<User, Integer> getDefaultDao() {
        return userDao;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.warn("load user without password...");
        User user = userDao.findByUsername(username);
        if(user == null){
            throw new UsernameNotFoundException("用户名不存在");
        }
        return transform(user);
    }


    @Override
    public UserDetails loadByUsernameAndPassword(String username,String password) throws UsernameNotFoundException {
        //查询时候已判断用户状态，无需再次递交下层判断
        User user = userDao.findByUsername(username);
        if(user == null){
            throw new UsernameNotFoundException("用户名不存在");
        }
        if(!passwordEncoder.matches(password,user.getPassword())){
            throw new BadCredentialsException("密码错误");
        }

        return transform(user);
    }

    private UserDetails transform(User user){
        List<Role> roles = roleDao.findByUserId(user.getId());
        if(CollectionUtils.isEmpty(roles)){
            throw new AuthenticationServiceException("账户未授权");
        }
        UserInfo userInfo = UserInfo.builder()
                .id(user.getId())
                .username(user.getUsername())
                .name(user.getName())
                .email(user.getEmail())
                .headImg(user.getHeadImg())
                .roles(roles.stream().map(x -> x.getRole()).collect(Collectors.toList()))
                .envs(roleService.findEnvByRole(roles))
                .menus(roleService.findMenuByRole(roles))
                .build();
        if(CollectionUtils.isEmpty(userInfo.getEnvs()) || CollectionUtils.isEmpty(userInfo.getMenus())){
            throw new InternalAuthenticationServiceException("用户未开通任何权限");
        }
        return new SecurityUser(userInfo);
    }
}
