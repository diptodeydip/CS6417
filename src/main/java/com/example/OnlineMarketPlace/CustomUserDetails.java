package com.example.OnlineMarketPlace;

import com.example.OnlineMarketPlace.model.AppUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetails {

    private AppUser user;

    public CustomUserDetails(AppUser user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
         GrantedAuthority authority = new SimpleGrantedAuthority(user.getRole());
         //GrantedAuthority authority1 = new SimpleGrantedAuthority(Commons.MFA_VERIFIED);
        //grantedAuthorities.add(authority1);
         grantedAuthorities.add(authority);

        return grantedAuthorities;
    }

    @Override
    public String getPassword() {
        if(user != null) {
            return user.getPassword();
        }
        return "";
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public AppUser getUser(){
        return user;
    }

}