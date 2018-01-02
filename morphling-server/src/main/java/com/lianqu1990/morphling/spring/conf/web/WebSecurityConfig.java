package com.lianqu1990.morphling.spring.conf.web;

import com.google.common.collect.SetMultimap;
import com.lianqu1990.morphling.common.consts.WebParamConsts;
import com.lianqu1990.morphling.service.local.MenuService;
import com.lianqu1990.morphling.spring.security.CaptchaFilter;
import com.lianqu1990.morphling.spring.security.CustomDaoAuthenticationProvider;
import com.lianqu1990.morphling.spring.security.UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.InMemoryTokenRepositoryImpl;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author hanchao
 * @date 2017/10/23 21:46
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private AuthenticationProvider authenticationProvider;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private MenuService menuService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry expressionInterceptUrlRegistry = http.csrf().disable()
                .authorizeRequests();

        SetMultimap<String, String> urlRoleMapping = menuService.getUrlRoleMapping();
        List<String> urls = urlRoleMapping.keySet().stream().sorted(Comparator.reverseOrder()).collect(Collectors.toList());
        for (String url : urls) {
            Set<String> roles = urlRoleMapping.get(url);
            expressionInterceptUrlRegistry = expressionInterceptUrlRegistry.antMatchers(url).hasAnyRole(roles.stream().toArray(String[]::new));
        }



        expressionInterceptUrlRegistry
                .antMatchers("/auth/**")
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                .loginPage("/auth/tologin")
                .loginProcessingUrl("/auth/login")
                .successForwardUrl("/auth/success?login")
                .failureForwardUrl("/auth/fail")
                .and()
                .logout()
                .logoutUrl("/auth/logout")
                .logoutSuccessUrl("/auth/success?logout")
                .and()
                .rememberMe()
                .rememberMeCookieName(WebParamConsts.REMEMBER_ME_COOKIE)
                .rememberMeParameter(WebParamConsts.REMEMBER_ME_PARAM)
                .key("morphling")
                .tokenRepository(new InMemoryTokenRepositoryImpl())
                .userDetailsService(userDetailsService)
                .and()
                .exceptionHandling()
                .accessDeniedPage("/auth/denied")
                .and()
                .addFilterBefore(new CaptchaFilter("/auth/login"), UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        //取消拦截静态资源
        web.ignoring().antMatchers("/","/**/*.html","/**/*.css","/**/*.js","/**/*.ico","/img/**","/fonts/**","/l10n/**","/**/*.woff")
                .antMatchers("/validcode/img","/echo/**","/download/**");
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider);
    }

    /**
     * controller中使用
     * @return
     */
    public AuthenticationManager getAuthenticationManager(){
        try {
            return this.authenticationManager();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


    @Bean
    public CustomDaoAuthenticationProvider authenticationProvider(@Autowired UserDetailsService userDetailService){
        CustomDaoAuthenticationProvider authenticationProvider = new CustomDaoAuthenticationProvider(userDetailService);
        return authenticationProvider;
    }

}
