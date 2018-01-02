package com.lianqu1990.morphling.spring.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * @author hanchao
 * @date 2017/11/6 13:02
 */
public class CustomDaoAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {
    private UserDetailsService userDetailsService;
    public CustomDaoAuthenticationProvider(UserDetailsService userDetailsService){
        this.userDetailsService = userDetailsService;
        this.setHideUserNotFoundExceptions(false);
    }
    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        //使用自定义的用户加载流程，此处无需继续验证
    }

    @Override
    protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        AuthenticationException authenticationException = AuthExceptionHolder.get();
        if(authenticationException != null){
            throw authenticationException;
        }
        return userDetailsService.loadByUsernameAndPassword(username,authentication.getCredentials().toString());
    }
}
