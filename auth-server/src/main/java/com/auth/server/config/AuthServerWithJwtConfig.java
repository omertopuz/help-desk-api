package com.auth.server.config;

import com.auth.server.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.web.cors.CorsConfiguration;

import javax.sql.DataSource;
import java.security.KeyPair;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableConfigurationProperties(SecurityProperties.class)
@EnableAuthorizationServer
public class AuthServerWithJwtConfig implements AuthorizationServerConfigurer {

    private final DataSource dataSource;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final SecurityProperties securityProperties;
    private final UserDetailsService userDetailsService;

    private JwtAccessTokenConverter jwtAccessTokenConverter;
    private TokenStore tokenStore;

    public AuthServerWithJwtConfig(final DataSource dataSource, final PasswordEncoder passwordEncoder,
                                            final AuthenticationManager authenticationManager, final SecurityProperties securityProperties,
                                            final UserDetailsService userDetailsService) {
        this.dataSource = dataSource;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.securityProperties = securityProperties;
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public TokenStore tokenStore() {
        if (tokenStore == null) {
            tokenStore = new JwtTokenStore(jwtAccessTokenConverter());
        }
        return tokenStore;
    }

    @Bean
    public DefaultTokenServices tokenServices(final TokenStore tokenStore,
                                              final ClientDetailsService clientDetailsService) {
        DefaultTokenServices tokenServices = new DefaultTokenServices();
        tokenServices.setSupportRefreshToken(true);
        tokenServices.setTokenStore(tokenStore);
        tokenServices.setClientDetailsService(clientDetailsService);
        tokenServices.setAuthenticationManager(this.authenticationManager);
        return tokenServices;
    }

    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        if (jwtAccessTokenConverter != null) {
            return jwtAccessTokenConverter;
        }

        SecurityProperties.JwtProperties jwtProperties = securityProperties.getJwt();
        KeyPair keyPair = keyPair(jwtProperties, keyStoreKeyFactory(jwtProperties));

        jwtAccessTokenConverter = new JwtAccessTokenConverter();
        jwtAccessTokenConverter.setKeyPair(keyPair);
        return jwtAccessTokenConverter;
    }

    @Override
    public void configure(final ClientDetailsServiceConfigurer clients) throws Exception {
        clients

                .jdbc(this.dataSource)

        ;
    }

    @Override
    public void configure(final AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints.authenticationManager(this.authenticationManager)
                .accessTokenConverter(jwtAccessTokenConverter())
                .userDetailsService(this.userDetailsService)
                .tokenStore(tokenStore());
    }

    @Override
    public void configure(final AuthorizationServerSecurityConfigurer oauthServer) {
        oauthServer
                .allowFormAuthenticationForClients()
                .passwordEncoder(this.passwordEncoder).tokenKeyAccess("permitAll()")
                .checkTokenAccess("isAuthenticated()")
//                .allowFormAuthenticationForClients()
        ;
//                .checkTokenAccess("permitAll()");
    }

    private KeyPair keyPair(SecurityProperties.JwtProperties jwtProperties, KeyStoreKeyFactory keyStoreKeyFactory) {
        return keyStoreKeyFactory.getKeyPair(jwtProperties.getKeyPairAlias(), jwtProperties.getKeyPairPassword().toCharArray());
    }

    private KeyStoreKeyFactory keyStoreKeyFactory(SecurityProperties.JwtProperties jwtProperties) {
        return new KeyStoreKeyFactory(jwtProperties.getKeyStore(), jwtProperties.getKeyStorePassword().toCharArray());
    }



/*
    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final DataSource dataSource;
    private final UserInfoService userInfoService;
    private final SecurityProperties securityProperties;

    private JwtAccessTokenConverter jwtAccessTokenConverter;
    private TokenStore tokenStore;

    public AuthServerWithJwtConfig(PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, DataSource dataSource, UserInfoService userInfoService, SecurityProperties securityProperties) {
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.dataSource = dataSource;
        this.userInfoService = userInfoService;
        this.securityProperties = securityProperties;
    }

    @Bean
    public TokenStore tokenStore() {
        if (tokenStore == null) {
            tokenStore = new JwtTokenStore(jwtAccessTokenConverter());
        }
        return tokenStore;
    }

    @Bean
    public DefaultTokenServices tokenServices() {
        DefaultTokenServices tokenServices = new DefaultTokenServices();
        tokenServices.setSupportRefreshToken(true);
        tokenServices.setReuseRefreshToken(false);
        tokenServices.setTokenStore(tokenStore);

        // to add additional info in token structure, at the end of token string
        tokenServices.setTokenEnhancer((accessToken, authentication)->{
            Map<String, Object> info = new HashMap<>();
            info.put("TEST", "TEST Enhancer");
            ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(info);

            return accessToken;
        });

        tokenServices.setAccessTokenValiditySeconds(securityProperties.getJwt().getDefaultAccessTokenTimeout());
        tokenServices.setRefreshTokenValiditySeconds(securityProperties.getJwt().getDefaultRefreshTokenTimeout());
//        tokenServices.setClientDetailsService(new JdbcClientDetailsService(dataSource));
        tokenServices.setAuthenticationManager(authentication -> {
            UserDetails userDetails = userInfoService.loadUserByUsername(authentication.getName());
            PreAuthenticatedAuthenticationToken token = new PreAuthenticatedAuthenticationToken(authentication.getName(), authentication.getCredentials(), userDetails.getAuthorities());
            token.setDetails(userDetails);
            return token;
        });

        return tokenServices;
    }
    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        if (jwtAccessTokenConverter != null) {
            return jwtAccessTokenConverter;
        }

        SecurityProperties.JwtProperties jwtProperties = securityProperties.getJwt();

//        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(new ClassPathResource(securityProperties.getJwt().getKeyStore()),
//                securityProperties.getJwt().getKeyStorePassword().toCharArray());
//        converter.setKeyPair(keyStoreKeyFactory.getKeyPair("demo"));
//
        KeyPair keyPair = keyPair(jwtProperties, keyStoreKeyFactory(jwtProperties));

        jwtAccessTokenConverter = new JwtAccessTokenConverter();
//        jwtAccessTokenConverter.setKeyPair(keyStoreKeyFactory.getKeyPair("keystore"));
        jwtAccessTokenConverter.setKeyPair(keyPair);

        return jwtAccessTokenConverter;
    }

    private KeyPair keyPair(SecurityProperties.JwtProperties jwtProperties, KeyStoreKeyFactory keyStoreKeyFactory) {
        return keyStoreKeyFactory.getKeyPair(jwtProperties.getKeyPairAlias(), jwtProperties.getKeyPairPassword().toCharArray());
    }

    private KeyStoreKeyFactory keyStoreKeyFactory(SecurityProperties.JwtProperties jwtProperties) {
        return new KeyStoreKeyFactory(jwtProperties.getKeyStore(), jwtProperties.getKeyStorePassword().toCharArray());
    }

    @Override
    public void configure(final ClientDetailsServiceConfigurer clients) throws Exception {
        clients.jdbc(this.dataSource);
    }

    @Override
    public void configure(final AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints.authenticationManager(this.authenticationManager)
                .accessTokenConverter(jwtAccessTokenConverter())
                .userDetailsService(this.userInfoService)
                .tokenStore(tokenStore())
        .tokenServices(tokenServices());
    }

    @Override
    public void configure(final AuthorizationServerSecurityConfigurer oauthServer) {
        oauthServer.passwordEncoder(this.passwordEncoder).tokenKeyAccess("permitAll()")
                .checkTokenAccess("isAuthenticated()");
    }
*/

}


