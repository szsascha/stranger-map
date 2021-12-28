package com.github.szsascha.strangermapbackend.service;

import com.github.szsascha.strangermapbackend.model.Peer;
import com.github.szsascha.strangermapbackend.util.Location;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.util.*;
import java.util.function.Consumer;

@Service
@Slf4j
public class PeerServiceImpl implements PeerService {

    @Value("${peer.radius}")
    private double RADIUS_KM;

    @Value("${peer.cleanup.timeout}")
    private long INACTIVITY_KICK_INTERVAL;

    private final Gson gson = new Gson();

    private final Jedis jedis = new Jedis();

    @Override
    public void cleanup() {
        log.info("CLEANUP: Remove all peers with inactivity > {} milliseconds", INACTIVITY_KICK_INTERVAL);
        final long currentTimeMillis = System.currentTimeMillis();
        iterateAllPeersAndDo(entry -> {
            if (currentTimeMillis - entry.getValue().getLastActivity() > INACTIVITY_KICK_INTERVAL) {
                log.info("CLEANUP: Remove peer {} with UUID {} due to inactivity", entry.getValue().getName(), entry.getKey());
                jedis.del(entry.getKey().toString());
            }
        });
    }

    @Override
    public UUID register(String name, String description) {
        final UUID uuid = UUID.randomUUID();
        if (containsKey(uuid)) {
            throw new IllegalStateException("Peer with UUID " + uuid + " already exists");
        }

        final Peer peer = Peer.builder()
                            .name(name)
                            .description(description)
                            .lastActivity(System.currentTimeMillis())
                            .build();

        setOrUpdatePeer(uuid, peer);

        return uuid;
    }

    @Override
    public void updatePosition(UUID uuid, double lat, double lon) {
        Peer peer = findPeerByUUID(uuid);
        peer.setLat(lat);
        peer.setLon(lon);
        peer.setLastActivity(System.currentTimeMillis());
        setOrUpdatePeer(uuid, peer);
    }

    @Override
    public List<Peer> getNearbyPeers(UUID uuid) {
        final Peer ownPeer = findPeerByUUID(uuid);
        final List<Peer> peersNearby = new ArrayList<>();

        iterateAllPeersAndDo(entry -> {
            if (entry.getKey().equals(uuid)) {
                // Do not use own peer
                return;
            }

            // TODO: Consider to do this inside of redis
            // Check if peer is nearby
            if (Location.distanceBetweenTwoLocationsInKm(
                    ownPeer.getLat(),
                    ownPeer.getLon(),
                    entry.getValue().getLat(),
                    entry.getValue().getLon()) <= RADIUS_KM) {
                peersNearby.add(entry.getValue());
            }
        });

        return peersNearby;
    }

    private Peer findPeerByUUID(UUID uuid) {
        String peerJson = jedis.get(uuid.toString());
        if (peerJson == null || peerJson.isEmpty()) {
            throw new IllegalStateException("No peer exists with UUID " + uuid);
        }

        Peer peer = gson.fromJson(peerJson, Peer.class);
        if (peer == null) {
            throw new IllegalStateException("Could not get peer with UUID " + uuid);
        }

        return peer;
    }

    private boolean containsKey(UUID uuid) {
        String peerJson = jedis.get(uuid.toString());
        return peerJson != null && !peerJson.isEmpty();
    }

    private void setOrUpdatePeer(UUID uuid, Peer peer) {
        jedis.set(uuid.toString(), gson.toJson(peer));
    }

    private void iterateAllPeersAndDo(Consumer<Map.Entry<UUID, Peer>> consumer) {
        Set<String> keys = jedis.keys("*");
        keys.forEach(key -> {
            final UUID uuid = UUID.fromString(key);
            consumer.accept(Map.entry(uuid, findPeerByUUID(uuid)));
        });
    }

}
