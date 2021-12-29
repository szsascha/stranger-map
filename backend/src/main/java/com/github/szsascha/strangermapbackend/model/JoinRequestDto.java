package com.github.szsascha.strangermapbackend.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class JoinRequestDto {

    @NotBlank
    private String name;

    @NotBlank
    private String description;

}
