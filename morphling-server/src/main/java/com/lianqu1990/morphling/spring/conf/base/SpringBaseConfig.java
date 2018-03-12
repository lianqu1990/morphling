package com.lianqu1990.morphling.spring.conf.base;

import com.lianqu1990.morphling.spring.security.CustomDaoAuthenticationProvider;
import com.lianqu1990.morphling.spring.security.UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


/**
 * @author hanchao
 * @date 2017/8/18 15:47
 */
@Configuration
@EnableAsync
@EnableScheduling
public class SpringBaseConfig {



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
