package com.example.core.events;

import java.util.ArrayList;
import java.util.List;

public abstract class AggregateRoot {

    private final List<DomainEvent> changes = new ArrayList<>();
    private Long version = 0L;

    public abstract String getId();

    protected void applyChange(DomainEvent event) {
        applyChange(event, true);
    }

    protected void applyChange(DomainEvent event, boolean isNew) {
        handle(event);
        if (isNew) {
            changes.add(event);
        }
    }

    protected abstract void handle(DomainEvent event);

    public List<DomainEvent> getUncommittedChanges() {
        return new ArrayList<>(changes);
    }

    public void loadFromHistory(List<DomainEvent> history) {
        history.forEach(event -> {
            applyChange(event, false);
            this.version = event.getVersion();
        });
    }

    public void markChangesAsCommitted() {
        changes.clear();
    }

    public Long getVersion() {
        return version;
    }

    protected void incrementVersion() {
        this.version++;
    }
}
