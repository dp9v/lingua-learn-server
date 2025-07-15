package com.cp9v.lingua_learn_server.api.dto;

import jakarta.validation.constraints.NotBlank;

import java.sql.ResultSet;
import java.sql.SQLException;

public record Group(
        Long id,

        @NotBlank(message = "Group name is required")
        String name,
        
        @NotBlank(message = "Group description is required")
        String description,
        boolean isPublic,
        long wordsCount
) {
    public Group(ResultSet rs, int i) throws SQLException {
        this(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getBoolean("is_public"),
                rs.getInt("words_count")
        );
    }
}
