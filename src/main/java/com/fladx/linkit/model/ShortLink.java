package com.fladx.linkit.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class ShortLink {
    @Id
    private Long id;

    private String originalUrl;
    private String shortUrl;
    private int clickCount;

    @Column(name = "user_id", nullable = false)
    private Long user;
}
