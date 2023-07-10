package com.example.back.Service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collection;

@Slf4j
public class AuthUtil {

    /**
     * 토큰에 저장되어 있는 id, nickname, role 값을 각각 얻을 수 있는 메소드
     */

    public static String getId() {
        
        CustomUserDetails principal = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return principal.getId();
    }
    public static String getNickname() {

        CustomUserDetails principal = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return principal.getUsername();
    }

    public static boolean hasRole(String role) {

        @SuppressWarnings("unchecked")
        Collection<GrantedAuthority> authorities = (Collection<GrantedAuthority>) SecurityContextHolder.getContext()
                .getAuthentication().getAuthorities();
        boolean hasRole = false;
        for (GrantedAuthority authority : authorities) {
            hasRole = authority.getAuthority().equals(role);
            if (hasRole) {
                break;
            }
        }
        return hasRole;
    }

    public static String getRole() {
        
        @SuppressWarnings("unchecked")
        Collection<GrantedAuthority> authorities =
                (Collection<GrantedAuthority>) SecurityContextHolder.getContext()
                        .getAuthentication()
                        .getAuthorities();
        String role = "";
        for (GrantedAuthority authority : authorities) {
            role = authority.getAuthority().replace("ROLE_", "");
        }
        return role;
    }

}
