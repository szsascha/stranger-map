package com.github.szsascha.strangermapbackend.controller;

import com.github.szsascha.strangermapbackend.model.JoinRequestDto;
import com.github.szsascha.strangermapbackend.model.JoinResponseDto;
import com.github.szsascha.strangermapbackend.service.PeerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/session")
public class SessionController {

    @Autowired
    private PeerService peerService;

    @PostMapping
    ResponseEntity<JoinResponseDto> join(@RequestBody JoinRequestDto joinRequest) {
        final UUID uuid = peerService.register(joinRequest.getName(), joinRequest.getDescription());
        return ResponseEntity.ok(new JoinResponseDto(uuid));
    }

}
