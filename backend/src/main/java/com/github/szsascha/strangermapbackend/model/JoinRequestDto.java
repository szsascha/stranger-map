package com.github.szsascha.strangermapbackend.model;

import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

@Data
public class JoinRequestDto {

    @NotBlank
    private String name;

    @NotBlank
    private String description;

}
