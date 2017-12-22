package com.huatu.morphling.spring.security;

import org.springframework.security.core.userdetails.UserDetails;

/**
 * @author hanchao
 * @date 2017/11/6 13:04
 */
public interface UserDetailsService extends org.springframework.security.core.userdetails.UserDetailsService{
    UserDetails loadByUsernameAndPassword(String username,String password);
}
