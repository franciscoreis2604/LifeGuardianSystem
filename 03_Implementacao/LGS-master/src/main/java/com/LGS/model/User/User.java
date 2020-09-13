package com.LGS.model.User;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.validation.constraints.NotBlank;
import java.util.*;

@Getter
@Setter
public class User implements UserDetails {

    @Setter(AccessLevel.NONE)
    private final UUID id;

    @NotBlank
    private String name;

    @NotBlank

    private String phoneNumber;

    @NotBlank

    private String username;

    @NotBlank
    private String password;

    private Date birthDate;

    @Setter
    private Set<? extends GrantedAuthority> grantedAuthorities;
    private boolean isAccountNonExpired;
    private boolean isAccountNonLocked;
    private boolean isCredentialsNonExpired;
    private boolean isEnabled;

    public User(UUID id,
                @JsonProperty("name") String name,
                @JsonProperty("email") String username,
                @JsonProperty("password") String password,
                @JsonProperty("phoneNumber") String phoneNumber,
                @JsonProperty("birthDate") Date birthDate,
                Set<? extends GrantedAuthority> grantedAuthorities,
                boolean isAccountNonExpired,
                boolean isAccountNonLocked,
                boolean isCredentialsNonExpired,
                boolean isEnabled) {

        this.id = id;
        this.name = name;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.birthDate = birthDate;
        this.grantedAuthorities = grantedAuthorities;
        this.username = username;
        this.isAccountNonExpired = isAccountNonExpired;
        this.isAccountNonLocked = isAccountNonLocked;
        this.isCredentialsNonExpired = isCredentialsNonExpired;
        this.isEnabled = isEnabled;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return grantedAuthorities;
    }

    public static Set<SimpleGrantedAuthority> setUserRoles(String role){
        Set<SimpleGrantedAuthority> permissions = new HashSet<>();
        permissions.add(new SimpleGrantedAuthority(role));
        return permissions;
    }


}
