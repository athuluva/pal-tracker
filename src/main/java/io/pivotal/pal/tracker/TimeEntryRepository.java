package io.pivotal.pal.tracker;

import java.util.List;

public interface TimeEntryRepository {

    TimeEntry create(TimeEntry date);
    TimeEntry find(long id);
    List<TimeEntry> list();

    TimeEntry update(long eq, TimeEntry timeEntry);
    void delete(long id);
}
