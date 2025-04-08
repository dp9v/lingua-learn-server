package com.cp9v.lingua_learn_server.api.models;

import java.sql.ResultSet;
import java.sql.SQLException;

public record User(
        long id,
        String email,
        String password,
        String name,
        String surname
) {

    public User(ResultSet rs, int i) throws SQLException {
        this(
                rs.getLong("id"),
                rs.getString("email"),
                "",
                rs.getString("name"),
                rs.getString("surname")
        );
    }
}
