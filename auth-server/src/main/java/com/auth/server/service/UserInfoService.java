package com.auth.server.service;

import com.auth.server.model.AuthUserInfo;
import com.auth.server.model.User;
import com.auth.server.repository.UserInfoRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AccountStatusUserDetailsChecker;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserInfoService implements UserDetailsService {

    @Autowired
    UserInfoRepo userInfoRepo;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User userInfo = userInfoRepo.findByUsername(s)
                .orElseThrow(()->new UsernameNotFoundException("Invalid username or password"));

        UserDetails userDetails = new AuthUserInfo(userInfo);
        new AccountStatusUserDetailsChecker().check(userDetails);
        return userDetails;
    }
}
