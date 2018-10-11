package io.pivotal.pal.tracker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
import java.util.List;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class JdbcTimeEntryRepository implements TimeEntryRepository{
    private JdbcTemplate jdbcTemplate;
    private TimeEntryRowMapper mapper = new TimeEntryRowMapper();

    private long project_id;
    private long user_id;
    private LocalDate date;
    private int hours;

    //private String insert = "INSERT INTO time_entries ('project_id',user_id,date,hours) VALUES (?,?,?,?)";
    private String retrieveRecord = "Select * from time_entries where id = ?";

    @Autowired
    public JdbcTimeEntryRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

   /* @Override
    public TimeEntry create(TimeEntry timeEntry) {
        final PreparedStatementCreator psc = new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(final Connection connection) throws SQLException {
                final PreparedStatement ps = connection.prepareStatement(insert,Statement.RETURN_GENERATED_KEYS);
                ps.setLong(1, timeEntry.getProjectId());
                ps.setLong(2,timeEntry.getUserId());
                ps.setDate(3, Date.valueOf(timeEntry.getDate()));
                ps.setInt(4,timeEntry.getHours());
                return ps;
            }
        };

        final KeyHolder holder = new GeneratedKeyHolder();

        jdbcTemplate.update(psc, holder);

        final long id = holder.getKey().longValue();

        return find(id);
    }*/

        @Override
        public TimeEntry create(TimeEntry timeEntry) {
            KeyHolder generatedKeyHolder = new GeneratedKeyHolder();

            jdbcTemplate.update(connection -> {
                PreparedStatement statement = connection.prepareStatement(
                        "INSERT INTO time_entries (project_id, user_id, date, hours) " +
                                "VALUES (?, ?, ?, ?)",
                        RETURN_GENERATED_KEYS
                );

                statement.setLong(1, timeEntry.getProjectId());
                statement.setLong(2, timeEntry.getUserId());
                statement.setDate(3, Date.valueOf(timeEntry.getDate()));
                statement.setInt(4, timeEntry.getHours());

                return statement;
            }, generatedKeyHolder);

            return find(generatedKeyHolder.getKey().longValue());
        }


    @Override
    public TimeEntry find(long id) {
            try {
                TimeEntry timeEntry = (TimeEntry)jdbcTemplate.queryForObject(retrieveRecord, new Object[] { id }, new TimeEntryRowMapper());

                return timeEntry;
            } catch (IncorrectResultSizeDataAccessException ex) {
                return null;
            }
    }

    @Override
    public List<TimeEntry> list() {
        return jdbcTemplate.query("SELECT id, project_id, user_id, date, hours FROM time_entries", new TimeEntryRowMapper());
    }

    @Override
    public TimeEntry update(long id, TimeEntry timeEntry) {
        jdbcTemplate.update("UPDATE time_entries " +
                        "SET project_id = ?, user_id = ?, date = ?,  hours = ? " +
                        "WHERE id = ?",
                timeEntry.getProjectId(),
                timeEntry.getUserId(),
                Date.valueOf(timeEntry.getDate()),
                timeEntry.getHours(),
                id);

        return find(id);
    }

    @Override
    public void delete(long id) {
        jdbcTemplate.update("DELETE FROM time_entries WHERE id = ?", id);
    }

    private final ResultSetExtractor<TimeEntry> extractor =
            (rs) -> rs.next() ? mapper.mapRow(rs, 1) : null;
}
