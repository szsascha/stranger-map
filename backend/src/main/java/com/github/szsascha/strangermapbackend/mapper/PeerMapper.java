package com.github.szsascha.strangermapbackend.mapper;

import com.github.szsascha.strangermapbackend.model.Peer;
import com.github.szsascha.strangermapbackend.model.PeerDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PeerMapper {

    PeerDto entity2Dto(Peer peer);

}
