package io.pivotal.pal.tracker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTimeEntryRepository implements TimeEntryRepository{

    private HashMap<Long, TimeEntry> cache;
    private long index;

    public InMemoryTimeEntryRepository() {
        cache = new HashMap<>();
        index = 1L;
    }

    public void delete(long timeEntryId) {
        cache.remove(timeEntryId);
    }

    public TimeEntry update(long id, TimeEntry newEntry){
        newEntry.setId(id);
        TimeEntry replacedEntry = cache.replace(id, newEntry);
        if (replacedEntry == null) {
            return null;
        } else {
            return newEntry;
        }
    }

    public TimeEntry create(TimeEntry any) {
        any.setId(index);
        cache.put(index, any);
        index++;
        return any;
    }

    public TimeEntry find(long timeEntryId) {

        return (TimeEntry) cache.get(timeEntryId);
    }

    public List list() {
        return new ArrayList(cache.values());
    }
}
