package com.github.szsascha.strangermapbackend.controller;

import com.github.szsascha.strangermapbackend.model.PeersDto;
import com.github.szsascha.strangermapbackend.model.SubmitPositionDto;
import com.github.szsascha.strangermapbackend.service.PeerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/peers")
public class PeerController {

    @Autowired
    private PeerService peerService;

    @PostMapping
    ResponseEntity<PeersDto> submitPositionAndGetPeers(@RequestBody SubmitPositionDto submitPositionDto) {
        peerService.updatePosition(
                submitPositionDto.getUuid(),
                submitPositionDto.getLat(),
                submitPositionDto.getLon()
        );
        return ResponseEntity.ok(peerService.getNearbyPeers(submitPositionDto.getUuid()));
    }

}
