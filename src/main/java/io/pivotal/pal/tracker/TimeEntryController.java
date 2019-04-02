package io.pivotal.pal.tracker;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Time;
import java.util.List;

@RestController
@RequestMapping("/time-entries")
public class TimeEntryController {

    private TimeEntryRepository repository;

    public TimeEntryController(TimeEntryRepository timeEntryRepository) {
        repository = timeEntryRepository;
    }

    @PostMapping
    public ResponseEntity<TimeEntry> create(@RequestBody TimeEntry timeEntryToCreate) {
        return new ResponseEntity<TimeEntry>(repository.create(timeEntryToCreate), HttpStatus.CREATED);

    }

    @GetMapping("/{id}")
    public ResponseEntity<TimeEntry> read(@PathVariable long id) {
        TimeEntry entry = repository.find(id);
        if (entry == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<TimeEntry>(entry, HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<List<TimeEntry>> list() {
        return new ResponseEntity<List<TimeEntry>>(repository.list(), HttpStatus.OK);
    }

    @PutMapping("/{nonExistentTimeEntryId}")
    public ResponseEntity<TimeEntry> update(@PathVariable long nonExistentTimeEntryId, @RequestBody TimeEntry timeEntry) {
        TimeEntry entry = repository.update(nonExistentTimeEntryId, timeEntry);
        if (entry == null) {
            return new ResponseEntity(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<TimeEntry>(entry, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable long id) {
        repository.delete(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
