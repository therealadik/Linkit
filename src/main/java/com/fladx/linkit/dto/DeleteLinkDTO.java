package com.fladx.linkit.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

/**
 * DTO for {@link com.fladx.linkit.model.Link}
 */
public record DeleteLinkDTO(
        @NotNull Long id,
        @NotNull UUID ownerUser) {
}