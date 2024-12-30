package com.cp9v.lingua_learn_server.bot.dto;

import com.cp9v.lingua_learn_server.bot.dto.enums.UpdateStatus;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RequiredArgsConstructor
@Builder
public class FlatUpdate {
    public final int updateId;
    public final int messageId;
    public final int chatId;
    public final int userId;
    public final String userName;
    public final LocalDateTime date;
    public final String text;
    public final UpdateStatus status;

    public FlatUpdate(ResultSet rs, int ignoredRowNum) throws SQLException {
        updateId = rs.getInt("update_id");
        messageId = rs.getInt("message_id");
        chatId = rs.getInt("chat_id");
        userId = rs.getInt("user_id");
        userName = rs.getString("username");
        date = LocalDateTime.parse(rs.getString("date"), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        text = rs.getString("text");
        status = UpdateStatus.valueOf(rs.getString("status"));
    }
}
