package io.pivotal.pal.tracker;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;

public class InMemoryTimeEntryRepository implements TimeEntryRepository {

    public Map<Object, Object> map = new HashMap();
    Long id = 1L;

    public TimeEntry create(TimeEntry timeEntry) {
       // timeEntry = new TimeEntry(1L, 123L, 456L, LocalDate.parse("2017-01-08"), 8);
        timeEntry.setId(id);
        map.put(id, timeEntry );
        id++;
        return timeEntry;
    }

    public TimeEntry find(long id) {
        return (TimeEntry)map.get(id);

    }

    public List<TimeEntry> list() {
        List<TimeEntry> list = asList( (TimeEntry)map.get(1L), (TimeEntry)map.get(2L));

        // using for-each loop for iteration over Map.entrySet()
        for (Map.Entry<Object, Object> entry : map.entrySet()) {

            list.add((TimeEntry)map.get(entry.getKey()));
        }
            /*System.out.println("Key = " + entry.getKey() +
                    ", Value = " + entry.getValue());*/

        /*new ArrayList<>() ;
        list.add(0,(TimeEntry)map.get(1L));
        list.add(1,(TimeEntry)map.get(2L));*/

        return list;
    }

    public TimeEntry update(long id, TimeEntry timeEntry) {
         map.remove(id);
        timeEntry.setId(id);
        map.put(id,timeEntry);

        return timeEntry;
    }

    public void delete(long id) {
        map.remove(id);
    }
}
