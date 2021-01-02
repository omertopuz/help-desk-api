package org.helpdesk.posts.config;

import org.helpdesk.posts.model.document.UserInfo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

import java.util.Optional;

@Configuration
@EnableMongoAuditing(auditorAwareRef = "auditorAware")
public class JpaConfig {
    @Bean
    public AuditorAware<UserInfo> auditorAware() {
        return ()->{
            return Optional.of(new UserInfo("user@abc.com","USER","testing purpose"));
            // Can use Spring Security to return currently logged in user
            // return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername()
        };
    }
}
