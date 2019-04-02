package io.pivotal.pal.tracker;

import java.util.List;

public interface TimeEntryRepository {
    public void delete(long timeEntryId);

    public TimeEntry update(long eq, TimeEntry any);

    public TimeEntry create(TimeEntry any);

    public TimeEntry find(long timeEntryId);

    public List list();
}
