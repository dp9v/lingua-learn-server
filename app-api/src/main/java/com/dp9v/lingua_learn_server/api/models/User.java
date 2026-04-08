package com.dp9v.lingua_learn_server.api.models;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Getter
@Builder
public final class User implements UserDetails {
    private final Long id;
    private final String email;
    private final String password;
    private final String name;
    private final String surname;

    public User(ResultSet rs, int i) throws SQLException {
        this(
                rs.getLong("id"),
                rs.getString("email"),
                rs.getString("password"),
                rs.getString("name"),
                rs.getString("surname")
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getUsername() {
        return email;
    }
}
