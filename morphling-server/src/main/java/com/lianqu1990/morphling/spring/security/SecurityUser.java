package com.lianqu1990.morphling.spring.security;

import com.lianqu1990.morphling.bean.UserInfo;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Assert;

import java.util.Collection;
import java.util.stream.Collectors;

import static com.lianqu1990.morphling.common.consts.SecurityConsts.ROLE_PREFIX;

/**
 * @author hanchao
 * @date 2017/11/6 10:56
 */
public class SecurityUser implements UserDetails,CredentialsContainer {

    private UserInfo userInfo;
    private Collection<? extends GrantedAuthority> authorities;

    public SecurityUser(UserInfo userInfo){
        Assert.notNull(userInfo,"userinfo cant be null");
        this.userInfo = userInfo;
        authorities = userInfo.getRoles().stream().map(role -> new SimpleGrantedAuthority(ROLE_PREFIX+role)).collect(Collectors.toSet());
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return userInfo.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public void eraseCredentials() {
        //nothing to do
    }
}
