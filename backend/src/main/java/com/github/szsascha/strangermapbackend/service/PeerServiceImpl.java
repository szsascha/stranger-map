package com.github.szsascha.strangermapbackend.service;

import com.github.szsascha.strangermapbackend.model.Peer;
import com.github.szsascha.strangermapbackend.model.PeerDto;
import com.github.szsascha.strangermapbackend.model.PeersDto;
import com.github.szsascha.strangermapbackend.util.Location;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PeerServiceImpl implements PeerService {

    @Value("${peer.radius}")
    private double RADIUS_KM;

    @Value("${peer.cleanup.timeout}")
    private long INACTIVITY_KICK_INTERVAL;

    private final Map<UUID, Peer> peers = new HashMap<>();

    @Override
    public void cleanup() {
        log.info("CLEANUP: Remove all peers with inactivity > {} milliseconds", INACTIVITY_KICK_INTERVAL);
        Set<Map.Entry<UUID, Peer>> peersToRemove = peers.entrySet().stream()
                .filter(entry -> System.currentTimeMillis() - entry.getValue().getLastActivity() > INACTIVITY_KICK_INTERVAL)
                .collect(Collectors.toSet());

        for (Map.Entry<UUID, Peer> entry : peersToRemove) {
            log.info("CLEANUP: Remove peer {} with UUID {} due to inactivity", entry.getValue().getName(), entry.getKey());
            peers.remove(entry.getKey());
        }
    }

    @Override
    public UUID register(String name, String description) {
        final UUID uuid = UUID.randomUUID();
        if (peers.containsKey(uuid)) {
            throw new IllegalStateException("Peer with UUID " + uuid + " already exists");
        }

        peers.put(
            uuid,
            Peer.builder()
                .name(name)
                .description(description)
                .lastActivity(System.currentTimeMillis())
                .build()
        );

        return uuid;
    }

    @Override
    public void updatePosition(UUID uuid, double lat, double lon) {
        Peer peer = findPeerByUUID(uuid);
        peer.setLat(lat);
        peer.setLon(lon);
        peer.setLastActivity(System.currentTimeMillis());
    }

    @Override
    public PeersDto getNearbyPeers(UUID uuid) {
        final Peer ownPeer = findPeerByUUID(uuid);
        return PeersDto.builder()
                .peers(
                        peers.entrySet().stream()
                            .filter(entry -> !entry.getKey().equals(uuid)) // Don't send own position
                            .filter(entry ->
                                    Location.distanceBetweenTwoLocationsInKm(
                                            ownPeer.getLat(),
                                            ownPeer.getLon(),
                                            entry.getValue().getLat(),
                                            entry.getValue().getLon()
                                    ) <= RADIUS_KM // Peer has to be in radius
                            )
                            .map(entry -> PeerDto.builder()
                                    .name(entry.getValue().getName())
                                    .description(entry.getValue().getDescription())
                                    .lat(entry.getValue().getLat())
                                    .lon(entry.getValue().getLon())
                                    .build()
                            )
                            .collect(Collectors.toList())
                )
                .build();
    }

    private Peer findPeerByUUID(UUID uuid) {
        return peers.entrySet().stream()
                .filter(entry -> entry.getKey().equals(uuid))
                .map(Map.Entry::getValue)
                .findAny()
                .orElseThrow(() -> new IllegalStateException("No peer exists with UUID " + uuid));
    }

}
