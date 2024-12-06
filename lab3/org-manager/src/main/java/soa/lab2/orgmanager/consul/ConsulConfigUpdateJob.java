package soa.lab2.orgmanager.consul;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ConsulConfigUpdateJob {
    private final ConsulSync consulSync;

    @Autowired
    public ConsulConfigUpdateJob(ConsulSync consulSync) {
        this.consulSync = consulSync;
    }

    @Scheduled(fixedRate = 10_000, initialDelay = 0)
    public void sync() {
        consulSync.syncServices();
    }

    @EventListener(ContextStartedEvent.class)
    public void doSomethingAfterStartup() {
        consulSync.syncServices();
    }
}
