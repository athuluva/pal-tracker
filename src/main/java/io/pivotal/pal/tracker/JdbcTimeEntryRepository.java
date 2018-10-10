package io.pivotal.pal.tracker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class JdbcTimeEntryRepository implements TimeEntryRepository{
    private JdbcTemplate jdbcTemplate;

    private String insert = "INSERT INTO time_entries (project_id,user_id,date,hours) VALUES";

    @Autowired
    public JdbcTimeEntryRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public TimeEntry create(TimeEntry date) {
        String insert_sql = insert+"("+date.getProjectId()+","+date.getUserId()+","+date.getDate()+","+date.getHours()+")";

        final PreparedStatementCreator psc = new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(final Connection connection) throws SQLException {
                final PreparedStatement ps = connection.prepareStatement(insert_sql,Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, name);
                return ps;
            }
        };



        jdbcTemplate.update(insert_sql);
        return null;
    }

    @Override
    public TimeEntry find(long id) {
        return null;
    }

    @Override
    public List<TimeEntry> list() {
        return null;
    }

    @Override
    public TimeEntry update(long eq, TimeEntry timeEntry) {
        return null;
    }

    @Override
    public void delete(long id) {

    }
}
