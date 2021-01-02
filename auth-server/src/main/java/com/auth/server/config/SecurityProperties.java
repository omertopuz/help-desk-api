package com.auth.server.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;

@ConfigurationProperties("security")
public class SecurityProperties {

    private JwtProperties jwt;

    public JwtProperties getJwt() {
        return jwt;
    }

    public void setJwt(JwtProperties jwt) {
        this.jwt = jwt;
    }

    public static class JwtProperties {

        private Resource keyStore;
//        private String keyStore;
        private String keyStorePassword;
        private String keyPairAlias;
        private String keyPairPassword;
        private Integer defaultAccessTokenTimeout;
        private Integer defaultRefreshTokenTimeout;
        private Integer failedLoginAttemptAccountLockTimeout;
        private Integer maxFailedLoginAttemptsForAccountLock;

        public Resource getKeyStore() {
            return keyStore;
        }

        public void setKeyStore(Resource keyStore) {
            this.keyStore = keyStore;
        }

        public String getKeyStorePassword() {
            return keyStorePassword;
        }

        public void setKeyStorePassword(String keyStorePassword) {
            this.keyStorePassword = keyStorePassword;
        }

        public String getKeyPairAlias() {
            return keyPairAlias;
        }

        public void setKeyPairAlias(String keyPairAlias) {
            this.keyPairAlias = keyPairAlias;
        }

        public String getKeyPairPassword() {
            return keyPairPassword;
        }

        public void setKeyPairPassword(String keyPairPassword) {
            this.keyPairPassword = keyPairPassword;
        }

        public Integer getDefaultAccessTokenTimeout() {
            return defaultAccessTokenTimeout;
        }

        public void setDefaultAccessTokenTimeout(Integer defaultAccessTokenTimeout) {
            this.defaultAccessTokenTimeout = defaultAccessTokenTimeout;
        }

        public Integer getDefaultRefreshTokenTimeout() {
            return defaultRefreshTokenTimeout;
        }

        public void setDefaultRefreshTokenTimeout(Integer defaultRefreshTokenTimeout) {
            this.defaultRefreshTokenTimeout = defaultRefreshTokenTimeout;
        }

        public Integer getFailedLoginAttemptAccountLockTimeout() {
            return failedLoginAttemptAccountLockTimeout;
        }

        public void setFailedLoginAttemptAccountLockTimeout(Integer failedLoginAttemptAccountLockTimeout) {
            this.failedLoginAttemptAccountLockTimeout = failedLoginAttemptAccountLockTimeout;
        }

        public Integer getMaxFailedLoginAttemptsForAccountLock() {
            return maxFailedLoginAttemptsForAccountLock;
        }

        public void setMaxFailedLoginAttemptsForAccountLock(Integer maxFailedLoginAttemptsForAccountLock) {
            this.maxFailedLoginAttemptsForAccountLock = maxFailedLoginAttemptsForAccountLock;
        }
    }
}