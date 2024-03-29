package com.github.szsascha.strangermapbackend.controller;

import com.github.szsascha.strangermapbackend.mapper.PeerMapper;
import com.github.szsascha.strangermapbackend.model.Peer;
import com.github.szsascha.strangermapbackend.model.PeerDto;
import com.github.szsascha.strangermapbackend.model.PeersDto;
import com.github.szsascha.strangermapbackend.model.SubmitPositionDto;
import com.github.szsascha.strangermapbackend.service.PeerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/peers")
@Slf4j
public class PeerController {

    @Autowired
    private PeerService peerService;

    @Autowired
    private PeerMapper peerMapper;

    @PostMapping
    ResponseEntity<PeersDto> submitPositionAndGetPeers(@Valid @RequestBody SubmitPositionDto submitPositionDto) {
        log.info("Update position of peer {} to {}, {}", submitPositionDto.getUuid(), submitPositionDto.getLat(), submitPositionDto.getLon());
        peerService.updatePosition(
                submitPositionDto.getUuid(),
                submitPositionDto.getLat(),
                submitPositionDto.getLon()
        );

        final List<Peer> peersNearby = peerService.getNearbyPeers(submitPositionDto.getUuid());
        final List<PeerDto> peersNearbyAsDto = peersNearby.stream()
                .map(peerMapper::entity2Dto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(
                PeersDto.builder()
                        .peers(peersNearbyAsDto)
                        .build());
    }

}
