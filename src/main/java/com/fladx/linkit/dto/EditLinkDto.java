package com.fladx.linkit.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO for {@link com.fladx.linkit.model.Link}
 */
public record EditLinkDto(@NotNull Long id,
                          String originalUrl,
                          String shortUrl,
                          LocalDateTime expiredAt,
                          Integer limitExceeded,
                          Boolean isLocked,
                          @NotNull UUID ownerUser
                          ) {
}