package com.github.szsascha.strangermapbackend.configuration;

import com.github.szsascha.strangermapbackend.service.PeerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class SchedulerConfiguration {

    @Autowired
    private PeerService peerService;

    @Scheduled(fixedRateString = "${peer.cleanup.interval}")
    void peerCleanup() {
        peerService.cleanup();
    }

}
