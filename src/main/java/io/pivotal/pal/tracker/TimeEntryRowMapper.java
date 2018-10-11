package io.pivotal.pal.tracker;

import org.springframework.jdbc.core.RowMapper;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class TimeEntryRowMapper implements RowMapper
{
    public TimeEntry mapRow(ResultSet rs, int rowNum) throws SQLException {

        TimeEntry timeEntry = new TimeEntry();
        timeEntry.setId(rs.getLong("id"));
        timeEntry.setProjectId(rs.getLong("project_id"));
        timeEntry.setUserId(rs.getLong("user_id"));
        timeEntry.setDate(rs.getDate("date").toLocalDate());
        timeEntry.setHours(rs.getInt("hours"));
        return timeEntry;
    }

}