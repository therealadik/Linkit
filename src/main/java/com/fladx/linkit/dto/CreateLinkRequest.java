package com.fladx.linkit.dto;

import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.URL;

import java.util.UUID;

/**
 * DTO for {@link com.fladx.linkit.model.Link}
 */
public record CreateLinkRequest(
        @NotNull @URL String originalUrl,
        UUID userId,
        Integer limitExceeded
) {
}