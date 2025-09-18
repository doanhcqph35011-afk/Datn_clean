package com.example.datn_meta.Service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;
@Controller
@Service("auth")
public class AuthService {
//Thông Tin Xác Thực
    public Authentication getAuthentication(){
        return SecurityContextHolder.getContext().getAuthentication();
    }
    // tên đăng nhập
    public String getUsername(){
        return getAuthentication().getName();
    }
    // Trạng Thái xác thực
    public boolean isAuthenticated(){
        String username = getAuthentication().getName();
        return (username!=null&& !username.equals("anonymousUser"));
    }
    public List<String> getRoles(){
        return this.getAuthentication().getAuthorities().stream()
                .map( au -> au.getAuthority().substring(5)).toList();
    }

    public boolean hasAnyRoles (String... roleToCheck){
        var grantedRoles = this.getRoles();
        return Stream.of(roleToCheck).anyMatch(grantedRoles::contains);
    }
}
