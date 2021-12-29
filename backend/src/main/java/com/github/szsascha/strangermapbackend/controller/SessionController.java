package com.github.szsascha.strangermapbackend.controller;

import com.github.szsascha.strangermapbackend.model.JoinRequestDto;
import com.github.szsascha.strangermapbackend.model.JoinResponseDto;
import com.github.szsascha.strangermapbackend.service.PeerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/api/session")
@Slf4j
public class SessionController {

    @Autowired
    private PeerService peerService;

    @PostMapping
    ResponseEntity<JoinResponseDto> join(@Valid @RequestBody JoinRequestDto joinRequest) {
        final UUID uuid = peerService.register(joinRequest.getName(), joinRequest.getDescription());
        log.info("Peer {}, ({}) registered with UUID {}", joinRequest.getName(), joinRequest.getDescription(), uuid);

        return ResponseEntity.ok(new JoinResponseDto(uuid));
    }

}
