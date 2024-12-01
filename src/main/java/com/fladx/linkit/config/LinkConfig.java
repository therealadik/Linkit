package com.fladx.linkit.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class LinkConfig {

    @Value("${link.lifetime}")
    private int lifetime;
}
