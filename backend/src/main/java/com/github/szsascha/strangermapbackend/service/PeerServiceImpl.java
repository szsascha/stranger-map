package com.github.szsascha.strangermapbackend.service;

import com.github.szsascha.strangermapbackend.model.Peer;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import redis.clients.jedis.GeoCoordinate;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.args.GeoUnit;
import redis.clients.jedis.resps.GeoRadiusResponse;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PeerServiceImpl implements PeerService {

    @Value("${peer.radius}")
    private double RADIUS_KM;

    @Value("${peer.cleanup.timeout}")
    private long INACTIVITY_KICK_INTERVAL;

    @Value("${peer.data.prefix}")
    private String PEER_DATA_PREFIX;

    @Value("${peer.position.set}")
    private String PEER_POSITION_SET;

    private final Gson gson = new Gson();

    private final Jedis jedis = new Jedis();

    @Override
    public void cleanup() {
        log.info("CLEANUP: Remove all peers with inactivity > {} milliseconds", INACTIVITY_KICK_INTERVAL);
        final long currentTimeMillis = System.currentTimeMillis();
        iterateAllPeersAndDo(entry -> {
            if (currentTimeMillis - entry.getValue().getLastActivity() > INACTIVITY_KICK_INTERVAL) {
                final String uuidString = entry.getKey().toString();
                log.info("CLEANUP: Remove peer {} with UUID {} due to inactivity", entry.getValue().getName(), uuidString);
                jedis.del(PEER_DATA_PREFIX + uuidString);
                jedis.zrem(PEER_POSITION_SET, uuidString);
            }
        });

        // cleanup all position without peerdata
        jedis.zrange(PEER_POSITION_SET, 0, -1).forEach(member -> {
            if (!containsPeerInData(UUID.fromString(member))) {
                log.info("CLEANUP: Remove position of peer {} because no entry found in data", member);
                jedis.zrem(PEER_POSITION_SET, member);
            }
        });
    }

    @Override
    public UUID register(String name, String description) {
        final UUID uuid = UUID.randomUUID();
        if (containsPeerInData(uuid)) {
            throw new IllegalStateException("Peer with UUID " + uuid + " already exists");
        }

        setOrUpdatePeer(uuid, Peer.builder()
                .name(name)
                .description(description)
                .lastActivity(System.currentTimeMillis())
                .build());

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
        final List<GeoRadiusResponse> peersInRadius =
                jedis.georadiusByMember(PEER_POSITION_SET, uuid.toString(), RADIUS_KM, GeoUnit.KM);

        return peersInRadius.stream()
                .filter(geoRadiusResponse -> !geoRadiusResponse.getMemberByString().equals(uuid.toString()))
                .map(geoRadiusResponse -> findPeerByUUID(UUID.fromString(geoRadiusResponse.getMemberByString())))
                .collect(Collectors.toList());
    }

    private Peer findPeerByUUID(UUID uuid) {
        String peerJson = jedis.get(PEER_DATA_PREFIX + uuid.toString());
        if (peerJson == null || peerJson.isEmpty()) {
            throw new IllegalStateException("No peer exists with UUID " + uuid);
        }

        Peer peer = gson.fromJson(peerJson, Peer.class);
        if (peer == null) {
            throw new IllegalStateException("Could not get peer with UUID " + uuid);
        }

        final List<GeoCoordinate> geoCoordinateList = jedis.geopos(PEER_POSITION_SET, uuid.toString());
        if (geoCoordinateList.size() != 1 || geoCoordinateList.get(0) == null) {
            throw new IllegalStateException("Invalid position count for " + uuid);
        }

        final GeoCoordinate geoCoordinate = geoCoordinateList.get(0);
        peer.setLon(geoCoordinate.getLongitude());
        peer.setLat(geoCoordinate.getLatitude());

        return peer;
    }

    private boolean containsPeerInData(UUID uuid) {
        String peerJson = jedis.get(PEER_DATA_PREFIX + uuid.toString());
        return peerJson != null && !peerJson.isEmpty();
    }

    private void setOrUpdatePeer(UUID uuid, Peer peer) {
        jedis.set(PEER_DATA_PREFIX + uuid.toString(), gson.toJson(peer));
        jedis.geoadd(PEER_POSITION_SET, peer.getLon(), peer.getLat(), uuid.toString());
    }

    private void iterateAllPeersAndDo(Consumer<Map.Entry<UUID, Peer>> consumer) {
        Set<String> keys = jedis.keys(PEER_DATA_PREFIX + '*');
        keys.forEach(key -> {
            final UUID uuid = obtainUUIDFromDataKey(key);
            consumer.accept(Map.entry(uuid, findPeerByUUID(uuid)));
        });
    }

    private UUID obtainUUIDFromDataKey(String key) {
        return UUID.fromString(key.replaceAll(PEER_DATA_PREFIX, ""));
    }

}
