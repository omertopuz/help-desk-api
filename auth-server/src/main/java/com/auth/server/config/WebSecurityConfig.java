package com.auth.server.config;

import com.auth.server.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{

    @Autowired
    UserInfoService userInfoService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userInfoService).passwordEncoder(getPasswordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.formLogin()
                .and()
                .requestMatchers().antMatchers("/login",
                "/logout", "/oauth/authorize",
                "/oauth/confirm_access",
        "/h2-console/**")
                .and()
                .authorizeRequests().anyRequest().authenticated()
                //.loginPage("/sso-login").successHandler(authenticationSuccessHandler)
        ;
    }

//        @Override
//    protected void configure(HttpSecurity http) throws Exception {
////        super.configure(http);
//        http.
//                .antMatchers("/login").permitAll()
//                .antMatchers("/oauth/authorize").permitAll()
//                .antMatchers("/oauth/token").permitAll()
//                .antMatchers("/h2-console/**").permitAll()
////                .and().formLogin()
//        ;
//
//        http.csrf().disable();
//        http.headers().frameOptions().disable();
//    }


    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
           .antMatchers("/h2-console/**");
    }

    @Bean
    public PasswordEncoder getPasswordEncoder(){
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
//        return new PasswordEncoder() {
//            @Override
//            public String encode(CharSequence charSequence) {
//                return charSequence.toString();
//            }
//
//            @Override
//            public boolean matches(CharSequence charSequence, String s) {
//                return s.equals(charSequence.toString());
//            }
//        };
    }

    @Bean
    public AuthenticationManager getAuthenticationManager() throws Exception {
        return super.authenticationManagerBean();
    }
}
