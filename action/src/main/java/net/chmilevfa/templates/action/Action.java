package net.chmilevfa.templates.action;

import net.chmilevfa.templates.repository.Batch;

import java.time.Duration;

public abstract class Action<RQ, RS> {

    protected final Batch batch = new Batch();

    public abstract RS run(RQ request);

    public String name() {
        return this.getClass().getSimpleName();
    }

    public Duration retryPeriod() {
        return Duration.ZERO;
    }

    public Batch changes() {
        return batch;
    }
}
