package com.fladx.linkit.controller;

import com.fladx.linkit.dto.CreateLinkRequest;
import com.fladx.linkit.model.Link;
import com.fladx.linkit.service.LinkService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class LinkController {

    private final LinkService linkService;

    public LinkController(LinkService linkService) {
        this.linkService = linkService;
    }

    @PostMapping("/link/create")
    public ResponseEntity<Link> createLink(@RequestBody CreateLinkRequest createLinkRequest) {
        var link = linkService.createLink(createLinkRequest);

        return ResponseEntity.ok(link);
    }

    @GetMapping("/{shortLink}")
    public ResponseEntity<Void> redirectLink(@PathVariable String shortLink) {
       return linkService.redirect(shortLink);
    }
}
