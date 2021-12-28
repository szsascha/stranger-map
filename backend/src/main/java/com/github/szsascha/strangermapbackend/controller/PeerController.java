package com.github.szsascha.strangermapbackend.controller;

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

@RestController
@RequestMapping("/api/peers")
@Slf4j
public class PeerController {

    @Autowired
    private PeerService peerService;

    @PostMapping
    ResponseEntity<PeersDto> submitPositionAndGetPeers(@RequestBody SubmitPositionDto submitPositionDto) {
        log.info("Update position of peer {} to {}, {}", submitPositionDto.getUuid(), submitPositionDto.getLat(), submitPositionDto.getLon());
        peerService.updatePosition(
                submitPositionDto.getUuid(),
                submitPositionDto.getLat(),
                submitPositionDto.getLon()
        );
        return ResponseEntity.ok(peerService.getNearbyPeers(submitPositionDto.getUuid()));
    }

}
