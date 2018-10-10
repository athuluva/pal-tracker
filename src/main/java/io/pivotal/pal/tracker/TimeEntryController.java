package io.pivotal.pal.tracker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Time;
import java.time.LocalDate;
import java.util.List;

@RestController
@ResponseBody
public class TimeEntryController {

    private TimeEntryRepository timeEntryRepository;
    private TimeEntry timeEntry;
    ResponseEntity<TimeEntry> responseEntity;

    @Autowired
    public TimeEntryController(TimeEntryRepository timeEntryRepository) {
        this.timeEntryRepository = timeEntryRepository;
    }

    @PostMapping("/time-entries")
    public ResponseEntity<TimeEntry> create(@RequestBody TimeEntry timeEntryToCreate) {
        TimeEntry newtimeEntry = timeEntryRepository.create(timeEntryToCreate);
        if(newtimeEntry!=null) {
            responseEntity = new ResponseEntity<TimeEntry>(newtimeEntry, HttpStatus.CREATED);
        } else {
            responseEntity = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return responseEntity;
    }

    @GetMapping("/time-entries/{id}")
    public ResponseEntity<TimeEntry> read(@PathVariable("id") long id) {
        timeEntry = timeEntryRepository.find(id);
        if(timeEntry!=null) {
            responseEntity = new ResponseEntity<TimeEntry>(timeEntry, HttpStatus.OK);
        } else {
            responseEntity = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return responseEntity;
    }

    @GetMapping("/time-entries")
    public ResponseEntity<List<TimeEntry>> list() {
        List<TimeEntry> list = timeEntryRepository.list();
        ResponseEntity<List<TimeEntry>> listResponseEntity = null;
        return new ResponseEntity<List<TimeEntry>>(list, HttpStatus.OK);
    }

    @PutMapping("/time-entries/{id}")
    public ResponseEntity update(@PathVariable("id") long id, @RequestBody TimeEntry timeEntry) {
        timeEntry = timeEntryRepository.update(id, timeEntry);
        if(timeEntry!=null){
            responseEntity = new ResponseEntity<TimeEntry>(timeEntry, HttpStatus.OK);
        } else {
            responseEntity = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return responseEntity;
    }

    @DeleteMapping("/time-entries/{id}")
    public ResponseEntity<TimeEntry> delete(@PathVariable("id") long id) {
        timeEntryRepository.delete(id);
        timeEntry = timeEntryRepository.find(id);

        if(timeEntry==null){
            responseEntity = new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return responseEntity;
    }
}
