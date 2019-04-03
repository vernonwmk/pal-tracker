package io.pivotal.pal.tracker;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Time;
import java.util.List;

@RestController
@RequestMapping("/time-entries")
public class TimeEntryController {

    private TimeEntryRepository repository;
    private final DistributionSummary timeEntrySummary;
    private final Counter actionCounter;

    public TimeEntryController(TimeEntryRepository timeEntryRepository, MeterRegistry meterRegistry) {
        repository = timeEntryRepository;
        timeEntrySummary = meterRegistry.summary("timeEntry.summary");
        actionCounter = meterRegistry.counter("timeEntry.actionCounter");
    }

    @PostMapping
    public ResponseEntity<TimeEntry> create(@RequestBody TimeEntry timeEntryToCreate) {
        TimeEntry createdEntry = repository.create(timeEntryToCreate);
        actionCounter.increment();
        timeEntrySummary.record(repository.list().size());
        return new ResponseEntity<TimeEntry>(createdEntry, HttpStatus.CREATED);

    }

    @GetMapping("/{id}")
    public ResponseEntity<TimeEntry> read(@PathVariable long id) {
        TimeEntry entry = repository.find(id);
        actionCounter.increment();
        if (entry == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<TimeEntry>(entry, HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<List<TimeEntry>> list() {
        actionCounter.increment();
        return new ResponseEntity<List<TimeEntry>>(repository.list(), HttpStatus.OK);
    }

    @PutMapping("/{nonExistentTimeEntryId}")
    public ResponseEntity<TimeEntry> update(@PathVariable long nonExistentTimeEntryId, @RequestBody TimeEntry timeEntry) {
        TimeEntry entry = repository.update(nonExistentTimeEntryId, timeEntry);
        if (entry == null) {
            return new ResponseEntity(null, HttpStatus.NOT_FOUND);
        }
        actionCounter.increment();
        return new ResponseEntity<TimeEntry>(entry, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable long id) {
        repository.delete(id);
        actionCounter.increment();
        timeEntrySummary.record(repository.list().size());
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
