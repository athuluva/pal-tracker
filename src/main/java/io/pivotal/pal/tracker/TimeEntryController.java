package io.pivotal.pal.tracker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.metrics.CounterService;
import org.springframework.boot.actuate.metrics.GaugeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@ResponseBody
@RequestMapping("/time-entries")
public class TimeEntryController {

    private  CounterService counter;
    private  GaugeService gauge;
    //private TimeEntryRepository timeEntriesRepo;

    private TimeEntryRepository timeEntryRepository;
    private TimeEntry timeEntry;
    ResponseEntity<TimeEntry> responseEntity;


    public TimeEntryController(TimeEntryRepository timeEntryRepository) {
        this.timeEntryRepository = timeEntryRepository;
    }

    @Autowired
    public TimeEntryController(
            TimeEntryRepository timeEntriesRepo,
            CounterService counter,
            GaugeService gauge
    ) {
        this.timeEntryRepository = timeEntriesRepo;
        this.counter = counter;
        this.gauge = gauge;
    }

    @PostMapping
    public ResponseEntity<TimeEntry> create(@RequestBody TimeEntry timeEntryToCreate) {
        TimeEntry newtimeEntry = timeEntryRepository.create(timeEntryToCreate);
        counter.increment("TimeEntry.created");
        gauge.submit("timeEntries.count", timeEntryRepository.list().size());
        if(newtimeEntry!=null) {
            responseEntity = new ResponseEntity<TimeEntry>(newtimeEntry, HttpStatus.CREATED);
        } else {
            responseEntity = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return responseEntity;
    }

    @GetMapping("{id}")
    public ResponseEntity<TimeEntry> read(@PathVariable("id") long id) {
        timeEntry = timeEntryRepository.find(id);
        if(timeEntry!=null) {
            responseEntity = new ResponseEntity<TimeEntry>(timeEntry, HttpStatus.OK);
            counter.increment("TimeEntry.read");
        } else {
            responseEntity = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return responseEntity;
    }

    @GetMapping
    public ResponseEntity<List<TimeEntry>> list() {
        counter.increment("TimeEntry.read");
        List<TimeEntry> list = timeEntryRepository.list();
        ResponseEntity<List<TimeEntry>> listResponseEntity = null;
        return new ResponseEntity<List<TimeEntry>>(list, HttpStatus.OK);
    }

    @PutMapping("{id}")
    public ResponseEntity update(@PathVariable("id") long id, @RequestBody TimeEntry timeEntry) {
        timeEntry = timeEntryRepository.update(id, timeEntry);
        if(timeEntry!=null){
            responseEntity = new ResponseEntity<TimeEntry>(timeEntry, HttpStatus.OK);
            counter.increment("TimeEntry.read");
        } else {
            responseEntity = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return responseEntity;
    }

    @DeleteMapping("{id}")
    public ResponseEntity<TimeEntry> delete(@PathVariable("id") long id) {
        timeEntryRepository.delete(id);
        timeEntry = timeEntryRepository.find(id);
        counter.increment("TimeEntry.read");

        if(timeEntry==null){
            responseEntity = new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return responseEntity;
    }
}
